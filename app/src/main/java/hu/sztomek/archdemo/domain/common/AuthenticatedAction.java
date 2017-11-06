package hu.sztomek.archdemo.domain.common;

public class AuthenticatedAction implements Action {

    private final String uid;

    public AuthenticatedAction(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthenticatedAction that = (AuthenticatedAction) o;

        return uid != null ? uid.equals(that.uid) : that.uid == null;
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AuthenticatedAction{" +
                "uid='" + uid + '\'' +
                '}';
    }
}
