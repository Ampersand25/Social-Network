package domain;

import org.jetbrains.annotations.NotNull;

public class SocialNetworkGraph {
    private boolean[][] adjacentMatrix;
    private final int size;
    boolean[] visitedNodes;

    private Long getUserMaxId(@NotNull Iterable<User> users) {
        Long maxId = 0L;

        for(User user : users) {
            Long userId = user.getId();
            if(userId > maxId) {
                maxId = userId;
            }
        }

        return maxId;
    }

    private void setAdjacentMatrix(boolean value) {
        for(int i = 0; i < size; ++i) {
            for(int j = i; j < size; ++j) {
                adjacentMatrix[i][j] = adjacentMatrix[j][i] = value;
            }
        }
    }

    private void computeAdjacentMatrix(@NotNull Iterable<Friendship> friendships) {
        adjacentMatrix = new boolean[size][size];
        setAdjacentMatrix(false);

        for(Friendship friendship : friendships) {
            int x = friendship.getFirstFriend().getId().intValue();
            int y = friendship.getSecondFriend().getId().intValue();

            adjacentMatrix[x][y] = adjacentMatrix[y][x] = true;
        }
    }

    private void initVisitedNodes() {
        for(int i = 0; i < size; visitedNodes[i++] = false);
    }

    private void dfsVisit(int src) {
        visitedNodes[src] = true;
        for(int node = 0; node < size; ++node) {
            if(adjacentMatrix[node][src] && !visitedNodes[node]) {
                dfsVisit(node);
            }
        }
    }

    private int dfs() {
        int numberOfConnectedComponents = 0;

        visitedNodes = new boolean[size];
        initVisitedNodes();

        for(int i = 0; i < size; ++i) {
            if(!visitedNodes[i]) {
                ++numberOfConnectedComponents;
                dfsVisit(i);
            }
        }

        return numberOfConnectedComponents;
    }

    public SocialNetworkGraph(Iterable<User> users, Iterable<Friendship> friendships) {
        size = getUserMaxId(users).intValue() + 1;
        computeAdjacentMatrix(friendships);
    }

    public int numberOfCommunities() {
        return dfs();
    }
}
