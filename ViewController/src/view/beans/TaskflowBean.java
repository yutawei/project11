package view.beans;
//version3
public class TaskflowBean {
    public String router;
    public TaskflowBean() {
    }

    public String go1() {
        // Add event code here...
        return "gorouter1";
    }


    public void setRouter(String router) {
        this.router = router;
    }

    public String getRouter() {
        return "true";
    }
}
