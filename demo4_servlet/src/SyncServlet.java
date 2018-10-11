import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@WebServlet("/SyncServlet")
public class SyncServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        long t1 = System.currentTimeMillis();
        //执行业务代码,从request获取信息，返回给response
        doSomeThing(request, response);
        System.out.println("耗时：" + (System.currentTimeMillis() - t1));

    }

    private void doSomeThing(HttpServletRequest request, HttpServletResponse response){
        try {
            //模拟耗时操作
            TimeUnit.SECONDS.sleep(5);
            response.getWriter().append("done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
