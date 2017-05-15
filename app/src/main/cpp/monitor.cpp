//
// Created by Administrator on 2017/1/11.
//

#include "monitor.h"
#include "utils.h"

#define LOG_TAG "Native"

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

ProcessBase *g_process;

const char* g_userId;

JNIEnv* g_env;

//子进程有权限访问父进程的私有目录,在此建立跨进程通信的套接字文件
static const char* PATH = "/data/data/com.same.city.love/my.sock";
//服务名称
static const char* SERVICE_NAME = "com.same.city.love/com.juns.wechat.activity.MainActivity";

bool ProcessBase::create_channel( )
{
}

int ProcessBase::write_to_channel( void* data, int len )
{
    return write( m_channel, data, len );
}

int ProcessBase::read_from_channel( void* data, int len )
{
    return read( m_channel, data, len );
}

int ProcessBase::get_channel() const
{
    return m_channel;
}

void ProcessBase::set_channel( int channel_fd )
{
    m_channel = channel_fd;
}

ProcessBase::ProcessBase()
{

}

ProcessBase::~ProcessBase()
{
    close(m_channel);
}

Parent::Parent(JNIEnv *env, jobject jobj) : m_env(env)
{
    LOGE("<<new parent instance>>");

    m_jobj = env->NewGlobalRef(jobj);
}

Parent::~Parent()
{
    LOGE( "<<Parent::~Parent()>>" );

    g_process = NULL;
}

void Parent::do_work()
{
}

JNIEnv* Parent::get_jni_env() const
{
    return m_env;
}

jobject Parent::get_jobj() const
{
    return m_jobj;
}

/**
* 父进程创建通道,这里其实是创建一个客户端并尝试
* 连接服务器(子进程)
*/
bool Parent::create_channel()
{
    int sockfd;

    sockaddr_un addr;

    while( 1 )
    {
        sockfd = socket( AF_LOCAL, SOCK_STREAM, 0 );

        if( sockfd < 0 )
        {
            LOGE("<<Parent create channel failed>>");

            return false;
        }

        memset(&addr, 0, sizeof(addr));

        addr.sun_family = AF_LOCAL;

        strcpy( addr.sun_path, PATH );

        if( connect( sockfd, (sockaddr*)&addr, sizeof(addr)) < 0 )
        {
            close(sockfd);

            sleep(1);

            continue;
        }

        set_channel(sockfd);

        LOGE("<<parent channel fd %d>>", m_channel );

        break;
    }

    return true;
}

/**
* 子进程死亡会发出SIGCHLD信号,通过捕捉此信号父进程可以
* 知道子进程已经死亡,此函数即为SIGCHLD信号的处理函数.
*/
static void sig_handler( int signo )
{
    pid_t pid;

    int status;

//调用wait等待子进程死亡时发出的SIGCHLD
//信号以给子进程收尸，防止它变成僵尸进程
    pid = wait(&status);

    if( g_process != NULL )
    {
        g_process->on_child_end();
    }
}

void Parent::catch_child_dead_signal()
{
    LOGE("<<process %d install child dead signal detector!>>", getpid());

    struct sigaction sa;

    sigemptyset(&sa.sa_mask);

    sa.sa_flags = 0;

    sa.sa_handler = sig_handler;

    sigaction( SIGCHLD, &sa, NULL );
}

void Parent::on_child_end()
{
    LOGE("<<on_child_end:create a new child process>>");

    create_child();
}

bool Parent::create_child( )
{
    pid_t pid;

    if( (pid = fork()) < 0 )
    {
        return false;
    }
    else if( pid == 0 ) //子进程
    {
        LOGE("<<In child process,pid=%d>>", getpid() );

        Child child;

        ProcessBase& ref_child = child;

        ref_child.do_work();
    }
    else if( pid > 0 )  //父进程
    {
        LOGE("<<In parent process,pid=%d>>", getpid() );
    }

    return true;
}

bool Child::create_child( )
{
//子进程不需要再去创建子进程,此函数留空
    return false;
}

Child::Child()
{
    RTN_MAP.member_rtn = &Child::parent_monitor;
}

Child::~Child()
{
    LOGE("<<~Child(), unlink %s>>", PATH);

    unlink(PATH);
}

void Child::catch_child_dead_signal()
{
//子进程不需要捕捉SIGCHLD信号
    return;
}

void Child::on_child_end()
{
//子进程不需要处理
    return;
}

void Child::handle_parent_die( )
{
//子进程成为了孤儿进程,等待被Init进程收养后在进行后续处理
    while( getppid() != 1 )
    {
        usleep(500); //休眠0.5ms
    }

    close( m_channel );

//重启父进程服务
    LOGE( "<<parent died,restart now>>" );

    restart_parent();
}

void Child::restart_parent()
{
    LOGE("<<restart_parent enter>>");

/**
* TODO 重启父进程,通过am启动Java空间的任一组件(service或者activity等)即可让应用重新启动
*/
    execlp( "am",
            "am",
            "startservice",
            "--user",
            g_userId,
            "-n",
            SERVICE_NAME, //注意此处的名称
            (char *)NULL);
}

void* Child::parent_monitor()
{
    handle_parent_die();
}

void Child::start_parent_monitor()
{
    pthread_t tid;

    pthread_create( &tid, NULL, RTN_MAP.thread_rtn, this );
}

bool Child::create_channel()
{
    int listenfd, connfd;

    struct sockaddr_un addr;

    listenfd = socket( AF_LOCAL, SOCK_STREAM, 0 );

    unlink(PATH);

    memset( &addr, 0, sizeof(addr) );

    addr.sun_family = AF_LOCAL;

    strcpy( addr.sun_path, PATH );

    if( bind( listenfd, (sockaddr*)&addr, sizeof(addr) ) < 0 )
    {
        LOGE("<<bind error,errno(%d)>>", errno);

        return false;
    }

    listen( listenfd, 5 );

    while( true )
    {
        if( (connfd = accept(listenfd, NULL, NULL)) < 0 )
        {
            if( errno == EINTR)
                continue;
            else
            {
                LOGE("<<accept error>>");

                return false;
            }
        }

        set_channel(connfd);

        break;
    }

    LOGE("<<child channel fd %d>>", m_channel );

    return true;
}

void Child::handle_msg( const char* msg )
{
//TODO How to handle message is decided by you.
}

void Child::listen_msg( )
{
    fd_set rfds;

    int retry = 0;

    while( 1 )
    {
        FD_ZERO(&rfds);

        FD_SET( m_channel, &rfds );

        timeval timeout = {3, 0};

        int r = select( m_channel + 1, &rfds, NULL, NULL, &timeout );

        if( r > 0 )
        {
            char pkg[256] = {0};

            if( FD_ISSET( m_channel, &rfds) )
            {
                read_from_channel( pkg, sizeof(pkg) );

                LOGE("<<A message comes:%s>>", pkg );

                handle_msg( (const char*)pkg );
            }
        }
    }
}

void Child::do_work()
{
    start_parent_monitor(); //启动监视线程

    if( create_channel() )  //等待并且处理来自父进程发送的消息
    {
        listen_msg();
    }
}

/*
 * Class:     com_juns_wechat_processes_Watcher
 * Method:    createWatcher
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_juns_wechat_processes_Watcher_createWatcher
        (JNIEnv *env, jobject thiz, jstring user)
{
    g_process = new Parent( env, thiz );

    g_userId  = (const char*)jstringTostring(env, user);

    g_process->catch_child_dead_signal();

    if( !g_process->create_child() )
    {
        LOGE("<<create child error!>>");

        return JNI_FALSE;
    }

    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_com_juns_wechat_processes_Watcher_connectToMonitor
        (JNIEnv *env, jobject thiz)
{
    if( g_process != NULL )
    {
        if( g_process->create_channel() )
        {
            return JNI_TRUE;
        }

        return JNI_FALSE;
    }
}

JNIEXPORT jint JNICALL Java_com_juns_wechat_processes_Watcher_sendMsgToMonitor
        (JNIEnv *env, jobject thiz, jstring string)
{

}