package lesson3;

import java.util.*;
import java.lang.String;


public class HangmanManager {

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }



    private final Vector<String> wordBank = new Vector<>(Arrays.asList("a", "z", "boyaryshnik", "skornyakova", "kravchenko", "kuptsov", "krasinets", "vkusvill", "cafeteria", "gypsy", "beehive", "terror", "cycle", "direction", "formatting", "vow", "assurance", "march", "experimentation", "offence", "adamance", "rend", "dine", "titan", "artifice", "laboratory", "tangerine", "junk", "perimeter", "dinner", "folklore", "legal", "neptune", "rhythmic gymnastics", "oncoming vehicles priority"));
    Random rand = new Random();

    private static class Session {

        private Integer mistakes;
        final String word;
        private final Vector<Boolean> isOpen = new Vector<Boolean>();
        private final Vector<Character> triedLetters = new Vector<Character>();

        public Session(String word) {
            this.word = word;
            mistakes = 0;
            //isOpen.clear();
            for(int i = 0;i<word.length();i++){
                isOpen.add(false);
            }
        }

        public boolean checkLetter(Character c){
            return triedLetters.contains(c);
        }

        public Boolean guess(Character c){
            //System.out.println(word);
            boolean guessed = false;
            triedLetters.add(c);
            for(int i = 0;i<word.length();i++){
                if(word.charAt(i)==c){
                    guessed = true;
                    isOpen.set(i, true);
                }
            }
            if(!guessed){
                mistakes++;
            }
            return guessed;
        }

        public String getWord() {
            String curWord = "";
            for(int i = 0;i<word.length();i++){
                if(isOpen.get(i)) curWord += word.charAt(i);
                else curWord += "*";
            }
            return curWord;
        }

        public void unlockFirst(){
            for (int i = 0;i<isOpen.size();i++){
                if(!isOpen.get(i)){
                    guess(word.charAt(i));
                    break;
                }
            }
        }

        public Boolean checkVictory(){
            return !isOpen.contains(false);
        }

    }

    private final Vector<Session> sessions = new Vector<Session>();
    private int currentSession = 0;
    private int maxMistakes = 2;
    private int wins = 0;
    private int losses = 0;

    public void addSession(){
        sessions.add(new Session(wordBank.get(rand.nextInt(wordBank.size()))));
    }

    public Session getCurrentSession(){return sessions.get(currentSession);}

    public boolean switchSession(int newSession){
        if(sessions.size() > newSession){
            currentSession = newSession;
            return true;
        }
        else return false;
    }

    private final Scanner reader = new Scanner(System.in); // Reading from System.in
    //System.out.println("Enter a number: ");
    //int n = reader.nextInt();
    public void receiveCommand(){
        String command = reader.nextLine().toLowerCase();
        if(command.equals("new session") | command.equals("new")){
            addSession();
            switchSession(sessions.size()-1);
            if(!getCurrentSession().guess(' ')) getCurrentSession().mistakes--;
            System.out.println("New session created! Current session set to " + sessions.size());
            System.out.println("The word in the new session is: " + sessions.getLast().getWord());
            this.receiveCommand();
        }
        else if(command.equals("switch session") | command.equals("switch")){
            if(sessions.isEmpty()){
                System.out.println("No sessions present. Use 'new' to create a new session");
            }
            else {
                System.out.println("input a session to switch to: ");
                command = reader.nextLine().toLowerCase();
                //char c = command.charAt(0);
                while(true) {
                    if(!isNumeric(command)){
                        System.out.println("Invalid session number. Please input a number");
                        command = reader.nextLine().toLowerCase();
                        //c = command.charAt(0);
                    }
                    else if(!switchSession(Integer.parseInt(command) - 1)) {
                        System.out.println("Invalid session number. Please input a number less than " + (sessions.size() + 1));
                        command = reader.nextLine().toLowerCase();
                        //c = command.charAt(0);
                    }
                    else break;
                }
                System.out.println("Switched session to " + command);
            }

            this.receiveCommand();
        }

        else if(command.equals("guess")){
            if(sessions.isEmpty() | currentSession >= sessions.size()){
                System.out.println("Invalid session. Use 'switch' or 'new'");
            }
            else {
                System.out.println("input a letter you want to guess: ");
                command = reader.nextLine().toLowerCase();
                if (getCurrentSession().checkLetter(command.charAt(0))) {
                    System.out.println("This letter has already been used in this session. Try another one");
                } else if (getCurrentSession().guess(command.charAt(0))) {
                    System.out.println("Your guess was correct! The current word is:");
                    System.out.println(getCurrentSession().getWord());
                    if (getCurrentSession().checkVictory()) {
                        System.out.println("Congratulation, you won! \nSession removed. Use 'switch' to switch to a new session");
                        wins+=1;
                        //System.out.println(wins);
                        sessions.remove(currentSession);
                        switchSession(sessions.size() - 1);
                    }
                } else {
                    System.out.println("Your guess was incorrect! The current word is:");
                    System.out.println(getCurrentSession().getWord());
                    System.out.println("Mistakes left: " + (maxMistakes - getCurrentSession().mistakes));
                    if ((maxMistakes - getCurrentSession().mistakes) <= 0) {
                        System.out.println("You lost, better luck next time! \nSession removed. Use 'switch' to switch to a new session");
                        losses+=1;
                        sessions.remove(currentSession);
                        switchSession(sessions.size() - 1);
                    }
                }
            }
            this.receiveCommand();
        }
        else if(command.equals("guess whole")){
            System.out.println("input a word you want to guess: ");
            command = reader.nextLine().toLowerCase();
            if(getCurrentSession().word.equals(command)){
                System.out.println("Your guess was correct!");
                //System.out.println(getCurrentSession().getWord());


                    System.out.println("Congratulation, you won! \nSession removed. Use 'switch' to switch to a new session");
                    wins+=1;
                    sessions.remove(currentSession);

            }
            else{
                System.out.println("Your guess was incorrect! The current word is:");
                System.out.println(getCurrentSession().getWord());
                getCurrentSession().mistakes+=1;
                System.out.println("Mistakes left: "+(maxMistakes - getCurrentSession().mistakes));
                if((maxMistakes - getCurrentSession().mistakes)==0){
                    losses+=1;
                    System.out.println("You lost, better luck next time! \nSession removed. Use 'switch' to switch to a new session");
                    sessions.remove(currentSession);
                }
            }
            this.receiveCommand();
        }
        else if(command.equals("sessions") | command.equals("info")){
            System.out.println("Amount of active sessions: "+sessions.size());
            int c = 1;
            for(Session i : sessions){
                System.out.println("-----------------------------\nSession number " + c + ":");
                System.out.println(i.getWord() + "\nMistakes left: " + (maxMistakes-i.mistakes));
                c+=1;
            }
            System.out.println("You won "+wins+" sessions and lost "+losses+" of them");
            this.receiveCommand();
        }
        else if(command.equals("curr") | command.equals("status")){
            if(currentSession >= sessions.size()){
                System.out.println("Your current session is invalid. Use 'switch' to switch to a new session");
            }
            else {
                System.out.println("The current session is " + (currentSession + 1));
                System.out.println("The current word is " + getCurrentSession().getWord());
                System.out.println("Mistakes left: " + (maxMistakes - getCurrentSession().mistakes));
            }
            this.receiveCommand();
        }
        else if(command.equals("difficulty")){
            System.out.println("The current max amount of mistakes per word is " + maxMistakes);
            System.out.println("Input the max amount of mistakes per word that you would like to set:");
            command = reader.nextLine().toLowerCase();
            //char c = command.charAt(0);
            while (!isNumeric(command)){
                System.out.println("Invalid number. Please input a number");
                command = reader.nextLine().toLowerCase();
            }
            maxMistakes = Integer.parseInt(command);
            System.out.println("Max mistakes per word set to "+maxMistakes);
            System.out.println("Some sessions may have become invalid. Use 'info' for details");
            this.receiveCommand();
        }
        else if(command.equals("bank") | command.equals("word bank")){
            System.out.println("The words currently in the word bank are:");
            for (String i : wordBank){
                System.out.println(i);
            }
            System.out.println("There are currently " + wordBank.size() + " words in the word bank");
            receiveCommand();
        }
        else if(command.equals("help")){
            System.out.println("I can't be bothered to write all of them out, just DM me @ToyvoF or check the code :^)");
            receiveCommand();
        }
        else if(command.equals("hint")){
            if(sessions.isEmpty() | currentSession >= sessions.size()){
                System.out.println("Invalid session. Use 'switch' or 'new'");
            }
            else {
                getCurrentSession().unlockFirst();
                System.out.println("Hint received! The word is currently:");
                System.out.println(getCurrentSession().getWord());
            }
            receiveCommand();
        }
        else if(command.equals("stop")){
            System.out.println("Ending game. Thank you for playing!");
        }
        else {
            System.out.println("Unknown command");
            this.receiveCommand();
        }



    }
    public void initialise(){
        Collections.sort(wordBank);
        System.out.println("Welcome to Fedor Semenov's multi-session hangman game! use 'new' to create a new session or 'help' for a list of commands");
        receiveCommand();
    }


}
