package utils;

import domain.Friendship_Status;

public class FriendshipStatusConverter {
    static public String convertStatusToString(Friendship_Status status) {
        return switch(status) {
            case PENDING -> "PENDING";
            case ACCEPTED -> "ACCEPTED";
            default -> "DECLINED";
        };
    }

    static public Friendship_Status convertStringToStatus(String str) throws Exception {
        if(str.equalsIgnoreCase("PENDING")) {
            return Friendship_Status.PENDING;
        }
        else if(str.equalsIgnoreCase("ACCEPTED")) {
            return Friendship_Status.ACCEPTED;
        }
        else if(str.equalsIgnoreCase("DECLINED")) {
            return Friendship_Status.DECLINED;
        }
        throw new Exception("[!]Invalid friendship status!\n");
    }
}
