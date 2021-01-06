package webserver;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.*;
import java.net.ServerSocket;

import static org.junit.Assume.*;

public class WebServerTest {
    private static WebServer webServer;
    private static ServerSocket serverSocket;
    private static int clientSocketPort;
    private File file = new File(webServer.getINDEX());
    public WebServerTest() throws IOException {
    }

    @Test
    public void verifyFileCreation() {
        Assert.assertNotNull(file);
    }

    @Test
    public void verifyIfServerIsRunning(){
        assumeTrue(WebServer.getStatus() == 1);
        Assert.assertEquals(1, WebServer.getStatus());
    }

    @Test
    public void verifyIfServerIsStopped(){
        assumeTrue(WebServer.getStatus() == 2);
        Assert.assertEquals(2, WebServer.getStatus());
    }

    @Test
    public void verifyIfServerIsInMaintenance(){
        assumeTrue(WebServer.getStatus() == 3);
        Assert.assertEquals(3, WebServer.getStatus());
    }

    @Test
    public void verifyIndexPagePath(){
        Assert.assertEquals("C:\\Users\\CRW_GARAJ\\Desktop\\index.html", WebServer.getINDEX());
    }

    @Test
    public void verifyErrorPagePath(){
        Assert.assertEquals("C:\\Users\\CRW_GARAJ\\Desktop\\eroare.html", WebServer.getERROR());
    }

    @Test
    public void verifyStoppedPagePath(){
        Assert.assertEquals("C:\\Users\\CRW_GARAJ\\Desktop\\stop.html", WebServer.getSTOPPED());
    }

    @Test
    public void verifyMaintenancePagePath(){
        Assert.assertEquals("C:\\Users\\CRW_GARAJ\\Desktop\\mentenanta.html", WebServer.getMAINTENANCE());
    }
}