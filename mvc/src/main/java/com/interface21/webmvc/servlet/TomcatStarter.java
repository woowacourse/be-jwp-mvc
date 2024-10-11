package com.interface21.webmvc.servlet;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

public class TomcatStarter {

    public static final String WEBAPP_DIR_LOCATION = "app/src/main/webapp/";

    private final Tomcat tomcat;

    public TomcatStarter(final int port) {
        this(WEBAPP_DIR_LOCATION, port);
    }

    public TomcatStarter(final String webappDirLocation, final int port) {
        this.tomcat = new Tomcat();
        tomcat.setConnector(createConnector(port));

        final var docBase = new File(webappDirLocation).getAbsolutePath();
        final var context = (StandardContext) tomcat.addWebapp("", docBase);
        skipJarScan(context);
        skipClearReferences(context);
    }

    public void start() {
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new UncheckedServletException(e);
        }
    }

    public void stop() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            throw new UncheckedServletException(e);
        }
    }

    private Connector createConnector(final int port) {
        final var connector = new Connector();
        connector.setPort(port);
        return connector;
    }

    private void skipJarScan(final Context context) {
        final var jarScanner = (StandardJarScanner) context.getJarScanner();
        jarScanner.setScanClassPath(false);
    }

    private void skipClearReferences(final StandardContext context) {
        /**
         * https://tomcat.apache.org/tomcat-10.1-doc/config/context.html
         *
         * setClearReferencesObjectStreamClassCaches 번역
         * true인 경우 웹 응용 프로그램이 중지되면 Tomcat은 직렬화에 사용되는
         * ObjectStreamClass 클래스에서 웹 응용 프로그램에 의해 로드된
         * 클래스에 대한 SoftReference를 찾고 찾은 모든 SoftReference를 지웁니다.
         * 이 기능은 리플렉션을 사용하여 SoftReference를 식별하므로 Java 9 이상에서
         * 실행할 때 명령줄 옵션 -XaddExports:java.base/java.io=ALL-UNNAMED를 설정해야 합니다.
         * 지정하지 않으면 기본값인 true가 사용됩니다.
         *
         * ObjectStreamClass와 관련된 메모리 누수는 Java 19 이상, Java 17.0.4 이상 및
         * Java 11.0.16 이상에서 수정되었습니다.
         * 수정 사항이 포함된 Java 버전에서 실행할 때 확인이 비활성화됩니다.
         *
         * Amazon Corretto-17.0.6은 경고 메시지가 나옴.
         * 학습과 관련 없는 메시지가 나오지 않도록 관련 설정을 끈다.
         */
        context.setClearReferencesObjectStreamClassCaches(false);
        context.setClearReferencesRmiTargets(false);
        context.setClearReferencesThreadLocals(false);
    }
}
