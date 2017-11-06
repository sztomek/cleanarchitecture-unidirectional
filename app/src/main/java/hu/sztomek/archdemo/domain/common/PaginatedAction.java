package hu.sztomek.archdemo.domain.common;

public class PaginatedAction extends AuthenticatedAction {

    private final String orderBy;
    private final String lastKey;
    private final int pageSize;

    public PaginatedAction(String uid, String orderBy, String lastKey, int pageSize) {
        super(uid);
        this.orderBy = orderBy;
        this.lastKey = lastKey;
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getLastKey() {
        return lastKey;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PaginatedAction that = (PaginatedAction) o;

        if (pageSize != that.pageSize) return false;
        if (orderBy != null ? !orderBy.equals(that.orderBy) : that.orderBy != null) return false;
        return lastKey != null ? lastKey.equals(that.lastKey) : that.lastKey == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (orderBy != null ? orderBy.hashCode() : 0);
        result = 31 * result + (lastKey != null ? lastKey.hashCode() : 0);
        result = 31 * result + pageSize;
        return result;
    }

    @Override
    public String toString() {
        return "PaginatedAction{" +
                "orderBy='" + orderBy + '\'' +
                ", lastKey='" + lastKey + '\'' +
                ", pageSize=" + pageSize +
                '}';
    }
}
