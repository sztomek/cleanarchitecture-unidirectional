package hu.sztomek.archdemo.presentation.screens.landing.timezone.edit;

import hu.sztomek.archdemo.domain.common.AuthenticatedAction;
import hu.sztomek.archdemo.presentation.screens.landing.timezone.common.DeleteTimezoneAction;

public final class TimezoneEditUiActions {

    private TimezoneEditUiActions() {}

    public static GetTimezoneAction get(String userId, String timezoneId) {
        return new GetTimezoneAction(userId, timezoneId);
    }

    public static DeleteTimezoneAction delete(String userId, String timezoneId) {
        return new DeleteTimezoneAction(userId, timezoneId);
    }

    public static SaveTimezoneAction save(SaveTimezoneAction.Builder builder) {
        return builder.build();
    }

    public static class GetTimezoneAction extends AuthenticatedAction {

        private final String timezoneId;

        GetTimezoneAction(String userId, String timezoneId) {
            super(userId);
            this.timezoneId = timezoneId;
        }

        public String getTimezoneId() {
            return timezoneId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            GetTimezoneAction that = (GetTimezoneAction) o;

            return timezoneId != null ? timezoneId.equals(that.timezoneId) : that.timezoneId == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (timezoneId != null ? timezoneId.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "GetTimezoneAction{" +
                    super.toString() +
                    ", timezoneId='" + timezoneId + '\'' +
                    '}';
        }
    }

    public static class SaveTimezoneAction extends AuthenticatedAction {

        private final String id;
        private final String name;
        private final String city;
        private final int difference;

        SaveTimezoneAction(String userId, String id, String name, String city, int difference) {
            super(userId);
            this.id = id;
            this.name = name;
            this.city = city;
            this.difference = difference;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCity() {
            return city;
        }

        public int getDifference() {
            return difference;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            SaveTimezoneAction that = (SaveTimezoneAction) o;

            if (difference != that.difference) return false;
            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            return city != null ? city.equals(that.city) : that.city == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (id != null ? id.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (city != null ? city.hashCode() : 0);
            result = 31 * result + difference;
            return result;
        }

        @Override
        public String toString() {
            return "SaveTimezoneAction{" +
                    super.toString() +
                    ", id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", city='" + city + '\'' +
                    ", difference=" + difference +
                    '}';
        }

        public static class Builder {

            private String userId;
            private String id;
            private String name;
            private String city;
            private int difference;


            public Builder setUserId(String userId) {
                this.userId = userId;
                return this;
            }

            public Builder setId(String id) {
                this.id = id;
                return this;
            }

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setCity(String city) {
                this.city = city;
                return this;
            }

            public Builder setDifference(int difference) {
                this.difference = difference;
                return this;
            }

            SaveTimezoneAction build() {
                return new SaveTimezoneAction(userId, id, name, city, difference);
            }
        }
    }

}
