import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@WebServlet("/SSE")
public class SSEServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 这两个头一定要设置
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");

        for (int i = 0; i < 5; i++) {
            //还可以指定时间标识
            response.getWriter().write("event:yooo\n");
            //格式： data:+数据+2个回车
            response.getWriter().write("data:" + i + "\n\n");
            //每次都要flush()一下
            response.getWriter().flush();

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
