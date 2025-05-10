package model;

import java.io.Serializable;

public class Friendship implements Serializable {
    private int userId1;
    private int userId2;

    public Friendship(int userId1, int userId2) {
        this.userId1 = Math.min(userId1, userId2);
        this.userId2 = Math.max(userId1, userId2);
    }

    public int getUserId1() {
        return userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public boolean involves(int userId) {
        return userId == userId1 || userId == userId2;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Friendship)) return false;
        Friendship f = (Friendship) o;
        return userId1 == f.userId1 && userId2 == f.userId2;
    }

    @Override
    public int hashCode() {
        return userId1 * 31 + userId2;
    }
}
