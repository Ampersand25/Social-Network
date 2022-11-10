package domain;

import exception.RepoException;
import infrastructure.IRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jetbrains.annotations.NotNull;

public class SocialNetworkGraph {
    private boolean[][] adjacentMatrix;
    private final int size;
    boolean[] visitedNodes;
    IRepository<Long, User> userRepo;

    /**
     * Metoda privata de tip operand (rezultat) care verifica daca un utilizator (obiect de clasa User) cu identificatorul unic userId (dat ca si parametru de intrare pentru metoda) exista in reteaua de socializare<br>
     * Metoda intoarce/returneaza o valoare booleana (valoare logica de adevar, adica adevarat (true) sau fals (false))
     * @param userId obiect de clasa Long care reprezinta identificatorul utilizatorului a carui existenta in retea dorim sa o testam (cu alte cuvinte vrem sa verificam daca in reteaua de socializare exista un utilizator cu id-ul userId)
     * @return true daca utilizatorul cu id-ul egal cu parametrul formal/simbolic userId exista in reteaua de socializare<br>
     *         false daca utilizatorul cu id-ul egal cu parametrul formal/simbolic userId nu exista in reteaua de socializare
     */
    private boolean userExists(Long userId) {
        try {
            userRepo.search(userId);
            return true;
        } catch(RepoException ex) {
            return false;
        }
    }

    /**
     * Metoda privata de tipul Long care returneaza/intoarce cel mai mare id al unui user/utilizator dintr-o lista de utilizatori (obiecte de clasa User) data ca si parametru de intrare pentru metoda
     * @param users obiect iterabil (lista) de obiecte de clasa User (utilizatori valizi din reteaua de socializare)
     * @return valoare numerica de tip Long reprenzentand id-ul maxim al unui utilizator din lista furnizata metodei in momentul apelului
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
     * Metoda privata de tip void (functie procedurala (procedura)) care reseteaza atributul privat adjacentMatrix al obiectului de clasa SocialNetworkGraph pentru care se apeleaza metoda<br>
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
     * Metoda privata de tip void (nu returneaza/intoarce nicio valoare) care creeaza matricea de adiacenta a grafului retelei de socializare pentru care se apeleaza metoda<br>
     * Metoda populeaza initial matricea de adiacenta cu valori false iar ulterior marcheaza cu valori true liniile si coloanele corespunzatoare utilizatorilor din reteaua de socializare care sunt prieteni (se regasesc in obiectul iterabil friendships)<br>
     * Daca doi prieteni cu id-urile x si y sunt prieteni atunci adjacentMatrix[x][y] si adjacentMatrix[y][x] vor avea valoarea true<br>
     * Daca doi prieteni cu id-urile x si y nu sunt prieteni atunci adjacentMatrix[x][y] si adjacentMatrix[y][x] vor avea valoarea false
     * @param friendships lista de obiecte de clasa Friendship reprezentand relatiile de prietenie din reteaua de socializare
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
     * Metoda privata de tip void (procedura) care initializeaza vectorul visitedNodes care reprezinta nodurile vizitate din graful retelei de socializare<br>
     * Metoda populeazaz lista de noduri vizitate cu valoarea booleana false (marcheaza fiecare user/utilizator din cadrul retelei ca fiind nevizitat)
     */
    private void initVisitedNodes() {
        for(int i = 0; i < size; visitedNodes[i++] = false);
    }

    /**
     * Metoda publica de tip void care parcurge toate nodurile nevizitate accesibile din nodul src (nod sursa reprezentand nodul curent din cadrul parcurgerii) si le marcheaza ca fiind vizitate in lista visitedNodes<br>
     * Parcurgerea se realizeaza in adancime (DFS - Depth First Search)
     * @param src valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) reprezentand nodul curent care este parcurs (care a fost decoperit de parcurgerea in adancime)
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
     * Metoda privata de tip int (intoarce o valoare numerica intreaga) care returneaza numarul de comunitati dintr-o retea de socializare<br>
     * Metoda apeleaza metoda privata dfsVisit pentru fiecare nod nevizitat din graful retelei (pentru fiecare componenta conexa)
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) care este pozitiva (mai mare sau egala cu 0) si care reprezentand numarul de comunitati din reteaua de socializare (adica numarul de componente conexe din graful retelei reprezentat sub forma de matrice de adiacenta prin atributul privat adjacentMatrix)
     */
    private int dfs() {
        int numberOfConnectedComponents = 0;

        visitedNodes = new boolean[size];
        initVisitedNodes();

        for(int i = 0; i < size; ++i) {
            if(userExists((long)i) && !visitedNodes[i]) {
                ++numberOfConnectedComponents;
                dfsVisit(i);
            }
        }

        return numberOfConnectedComponents;
    }

    /**
     * Constructor public care primeste doi parametri: users (lista de utilizatori valizi din reteaua de socializare) si friendships (lista de prietenii valide din reteaua de socializare)
     * @param users lista de obiecte de clasa User reprezentand lista de useri/utilizatori din reteaua de socializare
     * @param friendships lista de obiecte de clasa Friendship reprezentand lista de prietenii din reteaua de socializare
     * @param userRepo obiect de clasa IRepository reprezentand repozitoriul de utilizatori din cadrul aplicatiei
     */
    public SocialNetworkGraph(Iterable<User> users, Iterable<Friendship> friendships, IRepository<Long, User> userRepo) {
        size = getUserMaxId(users).intValue() + 1;
        computeAdjacentMatrix(friendships);
        this.userRepo = userRepo;
    }

    /**
     * Metoda publica de tip int (intoarce o valoare numerica intreaga) care returneaza numarul de comunitati dintr-o retea de socializare<br>
     * Metoda apeleaza metoda privata dfs si intoarce/returneaza rezultatul acestei metode
     * @return valoare numerica intreaga cu semn (signed) pe 4 bytes/octeti (32 de biti) care este pozitiva (mai mare sau egala cu 0) si care reprezentand numarul de comunitati din reteaua de socializare (adica numarul de componente conexe din graful retelei reprezentat sub forma de matrice de adiacenta)
     */
    public int numberOfCommunities() {
        return dfs();
    }

    /**
     * Metoda privata care returneaza/intoarce o lista de valori numerice de tip Long reprezentand componenta conexa din care face parte nodul/varful src (dat ca si parametru de intrare metodei)<br>
     * Metoda parcurge graful retelei folosind tehnica de cautare in latime (BFS - Breadth First Search)
     * @param src valoare numerica intreaga care reprezinta nodul sursa (nodul curent din parcurgerea in latime)
     * @return lista de obiecte de clasa Long (identificatori) reprezentand id-urile prietenilor (obiecte de clasa User) care reprezinta componenta conexa care contine nodul/varful src
     */
    private @NotNull List<Long> bfsVisit(int src) {
        List<Long> community = new ArrayList<>();
        LinkedList<Long> queue = new LinkedList<Long>();

        visitedNodes[src] = true;
        queue.add((long)src);

        while(!queue.isEmpty()) {
            src = queue.poll().intValue();
            community.add((long)src);

            for(int node = 0; node < size; ++node) {
                if(adjacentMatrix[src][node] && !visitedNodes[node]) {
                    visitedNodes[node] = true;
                    queue.add((long)node);
                }
            }
        }

        return community;
    }

    /**
     * Metoda privata care parcurge un graf folosind tehnica BFS (se apeleaza metoda privata bfsVisit pentru fiecare nod/varf nevizitat din graful retelei (graf reprezentat sub forma unei matrici de adiacenta))<br>
     * O lista reprezinta o comunitate (adica toate id-urile utilizatorilor din comunitatea respectiva)<br>
     * O comunitate reprezinta o componenta conexa din graful retelei<br>
     * Lista de liste reprezinta lista tuturor comunitatilor retelei de socializare
     * @return lista de liste de obiecte de clasa Long reprezentand lista tuturor comunitatilor din reteaua de socializare (adica lista tuturor componentelor conexe din graful retelei)
     */
    private @NotNull List<List<Long>> bfs() {
        List<List<Long>> communities = new ArrayList<>();

        visitedNodes = new boolean[size];
        initVisitedNodes();

        for(int i = 0; i < size; ++i) {
            if(userExists((long)i) && !visitedNodes[i]) {
                communities.add(bfsVisit(i));
            }
        }

        return communities;
    }

    /**
     * Metoda publica care returneaza lista de comunitati din graful retelei de socializare<br>
     * Metoda apeleaza metoda privata bfs si returneaza/intoarce rezultatul acestei metode<br>
     * Metoda furnizeaza o lista de liste care contin id-urile prietenilor dintr-o comunitate (componenta conexa a grafului retelei)
     * @return lista de liste de obiecte de clasa Long reprezentand lista tuturor comunitatilor din reteaua de socializare (adica lista tuturor componentelor conexe din graful retelei de socializare)
     */
    public List<List<Long>> getAllCommunities() {
        return bfs();
    }
}
