package jarekjal;

import javax.enterprise.context.Dependent;

@Dependent
public class MyDependency {

    int cnt = 0;
    private String message = "Test message";

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
