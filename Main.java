import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Election election = new Election();
        while (true) {
            System.out.println("\n===== VOTING MANAGEMENT SYSTEM =====");
            System.out.println("1. Admin Panel");
            System.out.println("2. Register as Voter");
            System.out.println("3. Voting Booth");
            System.out.println("4. View Results");
            System.out.println("5. Exit");
            System.out.println("=====================================");
            System.out.print("Enter choice: ");
            int choice = getIntInput(sc);
            switch (choice) {
                case 1:
                    adminPanel(sc, election);
                    break;
                case 2:
                    registerVoter(sc, election);
                    break;
                case 3:
                    votingBooth(sc, election);
                    break;
                case 4:
                    election.displayResults();
                    break;
                case 5:
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void adminPanel(Scanner sc, Election election) {
        while (true) {
            System.out.println("\n===== ADMIN PANEL =====");
            System.out.println("1. Add Candidate");
            System.out.println("2. View All Candidates");
            System.out.println("3. Open Voting");
            System.out.println("4. Close Voting");
            System.out.println("5. Declare Winner");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");
            int choice = getIntInput(sc);
            switch (choice) {
                case 1 -> addCandidate(sc, election);
                case 2 -> election.displayCandidates();
                case 3 -> election.openVoting();
                case 4 -> election.closeVoting();
                case 5 -> election.declareWinner();
                case 6 -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addCandidate(Scanner sc, Election election) {
        System.out.println("\n===== ADD CANDIDATE =====");
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Candidate ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Party: ");
        String party = sc.nextLine();
        election.addCandidate(name, id, party);
    }

    private static void registerVoter(Scanner sc, Election election) {
        System.out.println("\n===== REGISTER VOTER =====");
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Voter ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Age: ");
        int age = getIntInput(sc);
        election.registerVoter(name, id, age);
    }

    private static void votingBooth(Scanner sc, Election election) {
        System.out.println("\n===== VOTING BOOTH =====");
        System.out.print("Enter Voter ID: ");
        String voterId = sc.nextLine();
        election.displayCandidates();
        System.out.print("Enter Candidate ID: ");
        String candidateId = sc.nextLine();
        election.castVote(voterId, candidateId);
    }

    private static int getIntInput(Scanner sc) {
        int num = -1;
        while (true) {
            try {
                num = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
        return num;
    }
}

class Election {
    private ArrayList<Candidate> candidates = new ArrayList<>();
    private ArrayList<Voter> voters = new ArrayList<>();
    private boolean isVotingOpen = false;
    public static int totalVotes = 0;

    public void addCandidate(String name, String id, String party) {
        for (Candidate c : candidates) {
            if (c.getId().equals(id)) {
                System.out.println("Candidate ID already exists!");
                return;
            }
        }
        candidates.add(new Candidate(name, id, party));
        System.out.println("Candidate added successfully!");
    }

    public void registerVoter(String name, String id, int age) {
        if (age < 18) {
            System.out.println("Voter must be at least 18 years old!");
            return;
        }
        for (Voter v : voters) {
            if (v.getId().equals(id)) {
                System.out.println("Voter ID already exists!");
                return;
            }
        }
        voters.add(new Voter(name, id, age));
        System.out.println("Voter registered successfully!");
    }

    public void openVoting() {
        if (isVotingOpen) {
            System.out.println("Voting is already open!");
            return;
        }
        isVotingOpen = true;
        System.out.println("Voting is now OPEN!");
    }

    public void closeVoting() {
        if (!isVotingOpen) {
            System.out.println("Voting is already closed!");
            return;
        }
        isVotingOpen = false;
        System.out.println("Voting is now CLOSED!");
    }

    public void castVote(String voterId, String candidateId) {
        if (!isVotingOpen) {
            System.out.println("Voting is not open!");
            return;
        }
        Voter voter = null;
        for (Voter v : voters) {
            if (v.getId().equals(voterId)) {
                voter = v;
                break;
            }
        }
        if (voter == null) {
            System.out.println("Voter ID not found!");
            return;
        }
        if (voter.getHasVoted()) {
            System.out.println("You have already voted!");
            return;
        }
        Candidate candidate = null;
        for (Candidate c : candidates) {
            if (c.getId().equals(candidateId)) {
                candidate = c;
                break;
            }
        }
        if (candidate == null) {
            System.out.println("Candidate ID not found!");
            return;
        }
        candidate.receiveVote();
        voter.castVote();
        totalVotes++;
        System.out.println("Vote cast successfully for " + candidate.getName() + "!");
    }

    public void displayCandidates() {
        System.out.println("--- Candidates ---");
        int i = 1;
        for (Candidate c : candidates) {
            System.out.printf("%d. %s | Party: %s | ID: %s\n", i++, c.getName(), c.getParty(), c.getId());
        }
        if (candidates.isEmpty()) {
            System.out.println("No candidates available.");
        }
    }

    public void displayResults() {
        if (isVotingOpen) {
            System.out.println("Voting is still open. Close voting to view results.");
            return;
        }
        System.out.println("===== RESULTS =====");
        for (Candidate c : candidates) {
            System.out.printf("%s  | %s | Votes: %d | %.1f%%\n",
                c.getName(), c.getParty(), c.getVoteCount(), c.getVotePercentage(totalVotes));
        }
        System.out.println("Total Votes: " + totalVotes);
    }

    public void declareWinner() {
        if (isVotingOpen) {
            System.out.println("Voting is still open. Close voting to declare winner.");
            return;
        }
        if (candidates.isEmpty()) {
            System.out.println("No candidates to declare winner.");
            return;
        }
        int maxVotes = -1;
        ArrayList<Candidate> winners = new ArrayList<>();
        for (Candidate c : candidates) {
            if (c.getVoteCount() > maxVotes) {
                maxVotes = c.getVoteCount();
                winners.clear();
                winners.add(c);
            } else if (c.getVoteCount() == maxVotes) {
                winners.add(c);
            }
        }
        System.out.println("===== WINNER =====");
        if (maxVotes == 0) {
            System.out.println("No votes cast. No winner.");
            return;
        }
        if (winners.size() == 1) {
            System.out.println("Winner: " + winners.get(0).getName() + " with " + maxVotes + " vote(s)!");
        } else {
            System.out.print("Tie between: ");
            for (int i = 0; i < winners.size(); i++) {
                System.out.print(winners.get(i).getName());
                if (i < winners.size() - 1) System.out.print(", ");
            }
            System.out.println(" (" + maxVotes + " vote(s) each)");
        }
    }
}

class Candidate extends Person {
    private final String party;
    private int voteCount = 0;

    public Candidate(String name, String id, String party) {
        super(name, id);
        this.party = party;
    }

    public String getParty() {
        return party;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void receiveVote() {
        voteCount++;
    }

    public double getVotePercentage(int totalVotes) {
        if (totalVotes == 0) return 0.0;
        return (voteCount * 100.0) / totalVotes;
    }
}

class Person {
    private final String name;
    private final String id;

    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDetails() {
        return "Name: " + name + " | ID: " + id;
    }
}

class Voter extends Person {
    private final int age;
    private boolean hasVoted = false;

    public Voter(String name, String id, int age) {
        super(name, id);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public void castVote() {
        this.hasVoted = true;
    }
}
