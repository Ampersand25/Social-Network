package domain;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jetbrains.annotations.NotNull;

public class SocialNetworkGraph {
    private boolean[][] adjacentMatrix;
    private final int size;
    boolean[] visitedNodes;

    /**
     * Metoda privata de tipul Long care returneaza/intoarce cel mai mare id al unui user/utilizator dintr-o lista de utilizatori (obiecte de clasa User) data ca si parametru de intrare pentru metoda
     * @param users - obiect iterabil (lista) de obiecte de clasa User (utilizatori din reteaua de socializare)
     * @return - o valoare numerica de tip Long reprenzentand id-ul maxim al unui utilizator din lista furnizata metodei in momentul apelului
     */
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

    /**
     * Metoda privata de tip void (functie procedurala (procedura)) care reseteaza atributul privat adjacentMatrix al obiectului de clasa SocialNetworkGraph pentru care se apeleaza metoda
     * Metoda zerorizeaza matricea adjacentMatrix (o populeaza cu valori booleene de false)
     */
    private void resetAdjacentMatrix() {
        for(int i = 0; i < size; ++i) {
            for(int j = i; j < size; ++j) {
                adjacentMatrix[i][j] = adjacentMatrix[j][i] = false;
            }
        }
    }

    /**
     * Metoda privata de tip void (nu returneaza/intoarce nicio valoare) care creeaza matricea de adiacenta a grafului retelei de socializare
     * @param friendships - lista de obiecte de clasa Friendship reprezentand relatiile de prietenie din reteaua de socializare
     */
    private void computeAdjacentMatrix(@NotNull Iterable<Friendship> friendships) {
        adjacentMatrix = new boolean[size][size];
        resetAdjacentMatrix();

        friendships.forEach(friendship -> {
            int x = friendship.getFirstFriend().getId().intValue();
            int y = friendship.getSecondFriend().getId().intValue();

            adjacentMatrix[x][y] = adjacentMatrix[y][x] = true;
        });
    }

    /**
     * Metoda privata de tip void (procedura) care initializeaza vectorul visitedNodes care reprezinta nodurile vizitate din graful retelei de socializare
     * Metoda populeazaz lista de noduri vizitate cu valoarea booleana false (marcheaza fiecare user/utilizator din cadrul retelei ca fiind nevizitat)
     */
    private void initVisitedNodes() {
        for(int i = 0; i < size; visitedNodes[i++] = false);
    }

    /**
     * Metoda publica de tip void care parcurge toate nodurile nevizitate accesibile din nodul src (nod sursa) si le marcheaza ca fiind vizitate in lista visitedNodes
     * Parcurgerea are loc in adancime (DFS - Depth First Search)
     * @param src - valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprezentand nodul din care pornim cautarea in adancime
     */
    private void dfsVisit(int src) {
        visitedNodes[src] = true;
        for(int node = 0; node < size; ++node) {
            if(adjacentMatrix[node][src] && !visitedNodes[node]) {
                dfsVisit(node);
            }
        }
    }

    /**
     * Metoda privata de tip int (intoarce o valoare numerica intreaga) care returneaza numarul de comunitati dintr-o retea de socializare
     * Metoda apeleaza metoda privata dfsVisit pentru fiecare nod nevizitat din graful retelei (pentru fiecare componenta conexa)
     * @return - valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) care este pozitiva (mai mare sau egala cu 0) si care reprezentand numarul de comunitati din reteaua de socializare (adica numarul de componente conexe din graful retelei reprezentat sub forma de matrice de adiacenta)
     */
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

    /**
     * Constructor public care primeste doi parametri: users si friendships
     * @param users - lista de obiecte de clasa User reprezentand lista de useri/utilizatori din reteaua de socializare
     * @param friendships - lista de obiecte de clasa Friendship reprezentand lista de prietenii din reteaua de socializare
     */
    public SocialNetworkGraph(Iterable<User> users, Iterable<Friendship> friendships) {
        size = getUserMaxId(users).intValue() + 1;
        computeAdjacentMatrix(friendships);
    }

    /**
     * Metoda publica de tip int (intoarce o valoare numerica intreaga) care returneaza numarul de comunitati dintr-o retea de socializare
     * Metoda apeleaza metoda privata dfs si intoarce/returneaza rezultatul acestei metode
     * @return - valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) care este pozitiva (mai mare sau egala cu 0) si care reprezentand numarul de comunitati din reteaua de socializare (adica numarul de componente conexe din graful retelei reprezentat sub forma de matrice de adiacenta)
     */
    public int numberOfCommunities() {
        return dfs();
    }

    /**
     * Metoda privata care returneaza/intoarce o lista de valori numerice de tip Long reprezentand componenta conexa din care face parte nodul src (dat ca si parametru)
     * Metoda parcurge graful retelei folosind tehnica de cautare in latime (BFS - Breadth First Search)
     * @param src - valoare numerica intreaga care reprezinta nodul sursa (nodul curent din parcurgerea in latime)
     * @return - lista de obiecte de clasa Long reprezentand componenta conexa care contine nodul/varful src
     */
    private @NotNull List<Long> bfsVisit(int src) {
        List<Long> community = new ArrayList<>();
        LinkedList<Long> queue = new LinkedList<Long>();

        visitedNodes[src] = true;
        queue.add((long)src);

        while(!queue.isEmpty()) {
            src = queue.poll().intValue();
            community.add((long)src);

            for(int i = 0; i < size; ++i) {
                if(adjacentMatrix[src][i] && !visitedNodes[i]) {
                    visitedNodes[i] = true;
                    queue.add((long)i);
                }
            }
        }

        return community;
    }

    /**
     * Metoda privata care parcurge un graf folosind tehnica BFS (se apeleaza metoda privata bfsVisit pentru fiecare nod/varf nevizitat din graful retelei (graf reprezentat sub forma unei metrici de adiacenta))
     * @return - lista de liste de obiecte de clasa Long reprezentand lista tuturor comunitatilor din reteaua de socializare (adica lista tuturor componentelor conexe din graful retelei)
     */
    private @NotNull List<List<Long>> bfs() {
        List<List<Long>> communities = new ArrayList<>();

        visitedNodes = new boolean[size];
        initVisitedNodes();

        for(int i = 0; i < size; ++i) {
            if(!visitedNodes[i]) {
                communities.add(bfsVisit(i));
            }
        }

        return communities;
    }

    /**
     * Metoda publica care returneaza lista de comunitati din graful retelei de socializare
     * Metoda apeleaza metoda privata bfs si returneaza/intoarce rezultatul acestei metode
     * @return - lista de liste de obiecte de clasa Long reprezentand lista tuturor comunitatilor din reteaua de socializare (adica lista tuturor componentelor conexe din graful retelei)
     */
    public List<List<Long>> getAllCommunities() {
        return bfs();
    }
}
