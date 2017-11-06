package hu.sztomek.archdemo.presentation.navigation;

public interface IRouter {

    void restart();
    void close();
    void back();
    void toLogin();
    void toEmailLogin();
    void toEmailRegister(String email, int req);
    void toForgotPassword(String email);
    void toCheckUser();
    void toLanding();
    void showTimezoneList();
    void toCreateTimezone(String userId);
    void toEditTimezone(String userId, String tzid);

}
