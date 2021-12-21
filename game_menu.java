package assignment3;


import java.util.*;


// ->>Aryan Behal   ->>2019026   ->> Btech 2nd year  ->>Assignment-3..

// classes: Player, Mafia, Detective, Healer, Common, Mafia_the_party_game, game_menu(for main() ), sort_by_hp, sort_by_id
// Mafia, Detective, Healer, Common extend from Player...
// Mafia_the_party_game object in main()



class sort_by_hp implements java.util.Comparator<Player> {      //comparator for sorting mafia_list on hp

    @Override
    public int compare(Player o1, Player o2) {
        return (int) (o1.getHP()-o2.getHP());
    }
}
class sort_by_id implements java.util.Comparator<Player>    //comparator for sorting all list on basis of id
{
    @Override
    public int compare(Player o1,Player o2)
    {
        return o1.getId()- o2.getId();
    }
}

//blueprint for all players
abstract class Player {

    protected int id;               //id is static as i have provided no setter to change it in Player or any subclass.. and it is not public..
    protected float hp;
    protected ArrayList<? extends Player> same_list;     //same_list is used to make list of all types of players.. Generic programming
    protected Scanner in=new Scanner(System.in);


    //constructor
    public Player(int id,float hp)
    {
        this.id=id;
        this.hp=hp;
    }
    public void increaseHP(float hp_increase)
    { this.hp+=hp_increase; }

    public int give_vote(ArrayList<Player> players)        //for voting.. returns index in list of chosen player..
    {
        Random rand = new Random();
        return rand.nextInt(players.size()); }

    //abstract
    abstract int take_turn(Player user, ArrayList<Player> list);

    //setter
    public void set_common_list(ArrayList<? extends Player> list)
    { this.same_list=list; }

    public void setHP(float hp) {
        this.hp = hp; }
    public void setId(int id) {
        this.id = id; }

    //getter
    public int getId()
    { return this.id; }
    public float getHP()
    {return this.hp;}
}


class Detective extends Player
{
   public Detective(int id)
   { super(id,800); }

   //abstract function implementation
    public int take_turn(Player user,ArrayList<Player> list)             //return id of tested player..
    {
        int target=-1;
        ArrayList<Player> player_list=new ArrayList<Player>();
        for(int i=0;i<list.size();i++) {
            if (!(list.get(i) instanceof Detective))            //non detective players list
                player_list.add(list.get(i));
        }
        if(same_list.contains(user))
        {
            Boolean target_is_detective=true;
            int target_to_check=-1;
            while(target_is_detective) {
                Boolean target_present=false;
                while(!target_present) {
                    System.out.print("\nEnter player to test: ");
                    target_to_check = in.nextInt();
                    for (int i = 0; i < list.size(); i++) {
                        if (target_to_check == list.get(i).getId()) {
                            target_present = true;
                            target=i;
                            break;
                        }
                    }
                    if (!target_present) {
                        System.out.println("No such player/Player already killed..");
                    }
                }
                target_is_detective=false;
                for (int i = 0; i < same_list.size(); i++) {
                    if (same_list.get(i).equals(list.get(target))) {
                        target_is_detective = true;
                    }
                }
                if (target_is_detective) {
                    System.out.print("You cant choose a detective..");
                }
            }
            if(list.get(target) instanceof Mafia)
                return  list.get(target).getId();
            else
                return -1;
        }
        else
        {
            Random rand = new Random();
            target=rand.nextInt(player_list.size());
            if(player_list.get(target) instanceof Mafia)
            {
                return player_list.get(target).getId();
            }
            else
                return -1;
        }
    }
}

class Mafia extends Player{
    public Mafia(int id)
    {
        super(id,2500);
    }

    // abstract function implementation
    public int take_turn(Player user,ArrayList<Player> list)               //return id of player to be killed
    {
        int target=-1;
        float total_HP=0;
        ArrayList<Player> player_list=new ArrayList<Player>();
        for(int i=0;i<list.size();i++) {
            if (!(list.get(i) instanceof Mafia))            //non mafia player list
            {
                player_list.add(list.get(i));
            }
            else
            {
                total_HP+=list.get(i).getHP();
            }
        }
        if(same_list.contains(user)) {
            Boolean target_is_mafia = true;
            int target_to_kill=-1;
            while (target_is_mafia) {
                Boolean target_present=false;
                while(!target_present) {
                    System.out.print("\nEnter player to kill: ");
                    target_to_kill = in.nextInt();
                    for (int i = 0; i < list.size(); i++) {
                        if (target_to_kill == list.get(i).getId()) {
                            target_present = true;
                            target=i;
                            break;
                        }
                    }
                    if (!target_present) {
                        System.out.println("No such player/Player already killed..");
                    }
                }
                target_is_mafia = false;
                for (int i = 0; i < same_list.size(); i++) {
                    if (same_list.get(i).equals(list.get(target))) {
                        target_is_mafia = true;
                    }
                }
                if (target_is_mafia) {
                    System.out.print("You cant choose a mafia..");
                }
            }

        }
        else
        {
            Random rand = new Random();
            int chosen=rand.nextInt(player_list.size());
            for(int i=0;i<list.size();i++)
            {
                if(player_list.get(chosen).equals(list.get(i)))
                {
                    target=i;
                    break;
                }
            }
        }
        float target_hp=list.get(target).getHP();
        if(target_hp>total_HP)
        {
            list.get(target).setHP(target_hp-total_HP);
            target_hp=total_HP;
        }
        else
        {
            list.get(target).setHP(0);
        }
        Collections.sort(same_list,new sort_by_hp());               //sorting using comparator

        float ind_hp_damage=target_hp/ same_list.size();
        for(int i=0;i<same_list.size();i++)
        {
            if(same_list.get(i).getHP()>=ind_hp_damage)
            {
                same_list.get(i).setHP(same_list.get(i).getHP()-ind_hp_damage);
            }
            else
            {
                target_hp-=same_list.get(i).getHP();
                same_list.get(i).setHP(0);
                if(i+1!=same_list.size())
                    ind_hp_damage=target_hp/(same_list.size()-i-1);
            }
        }
        return list.get(target).getId();
    }
}

class Healer extends Player{
    public Healer(int id)
    {
        super(id,800);
    }

    //abstract function implementation
    public int take_turn(Player user, ArrayList<Player> list)              //returns 0 if successfully healed up a player
    {
        int target=-1;
        if(same_list.contains(user)) {
            Boolean target_is_present = false;
            while (!target_is_present) {
                System.out.print("\nEnter player to heal: ");
                target = in.nextInt();
                for (int i = 0; i < list.size(); i++) {
                    if (target == list.get(i).getId()) {
                        target_is_present = true;
                        target=i;
                        break;
                    }
                }
                if (!target_is_present) {
                    System.out.println("No such player/Player already killed..");
                }
            }
        }
        else
        {
            Random rand = new Random();
            int chosen=rand.nextInt(list.size());
            target=chosen;
        }
        this.heal_player(list.get(target));
        return 0;
    }
    public void heal_player(Player player)
    {
        player.increaseHP(500);
    }
}

class Common extends Player
{
    public Common(int id)
    {
        super(id,1000);
    }

    //abstract implementation
    public int take_turn(Player user, ArrayList<Player> list)
    {
        System.out.print("no special job");
        return 0;
    }
}

class Mafia_the_party_game{
    private int total_players;
    private int total_mafias;
    private int total_detectives;
    private int total_healers;
    private int total_commoners;
    private ArrayList<Player> players;
    private ArrayList<Mafia> mafias;
    private ArrayList<Detective> detectives;
    private ArrayList<Healer> healers;
    private ArrayList<Common> commons;
    private ArrayList<Player> removed_players;

    private Player user;
    private Scanner in=new Scanner(System.in);

    Mafia_the_party_game()   //default constructor
    { }
    public Mafia_the_party_game(int n)
    {
        total_players=n;
        players= new ArrayList<Player>(n);
        removed_players=new ArrayList<Player>(n);
        total_mafias=n/5;
        total_detectives=n/5;
        total_healers=Math.max(1, n/10);
        total_commoners=total_players-(total_detectives+total_mafias+total_healers);
        mafias=new ArrayList<Mafia>(total_mafias);
        detectives=new ArrayList<Detective>(total_detectives);
        healers=new ArrayList<>(total_healers);
        commons=new ArrayList<Common>(total_commoners);
    }


    private void allot_players()
    {
        boolean correct_choice=false;
        int choice=-1;
        int type=0;
        int player_allotted = 0;
        int i;
        ArrayList<Integer> allot_id=new ArrayList<Integer>(total_players);
        for(i=0;i<total_players;i++)
        {
            allot_id.add(i+1);
        }
        Collections.shuffle(allot_id);
        while(!correct_choice) {
            System.out.print("Choose a character:\n1. Mafia\n2. Detective\n3. Healer\n4. Commoner\n5. Assign randomly\n--> ");
            player_allotted = 0;
            type = 0;
            choice = in.nextInt();
            i=0;
            correct_choice=true;
            switch (choice) {
                case 1:
                    player_allotted++;
                    Mafia mafia = new Mafia(allot_id.remove(0));
                    user = mafia;
                    players.add(mafia);
                    mafias.add(mafia);
                    i = 2;
                    while (i <= total_mafias) {
                        player_allotted++;
                        mafia = new Mafia(allot_id.remove(0));
                        players.add(mafia);
                        mafias.add(mafia);

                        i++;
                    }
                    type = 1;
                    break;
                case 2:
                    player_allotted++;
                    Detective detective = new Detective(allot_id.remove(0));
                    user = detective;
                    players.add(detective);
                    detectives.add(detective);
                    i = 2;
                    while (i <= total_detectives) {
                        player_allotted++;
                        detective = new Detective(allot_id.remove(0));
                        players.add(detective);
                        detectives.add(detective);
                        i++;
                    }
                    type = 2;
                    break;
                case 3:
                    player_allotted++;
                    Healer healer = new Healer(allot_id.remove(0));
                    user = healer;
                    players.add(healer);
                    healers.add(healer);
                    i = 2;
                    while (i <= total_healers) {
                        player_allotted++;
                        healer = new Healer(allot_id.remove(0));
                        players.add(healer);
                        healers.add(healer);
                        i++;
                    }
                    type = 3;
                    break;
                case 4:
                    player_allotted++;
                    Common common = new Common(allot_id.remove(0));
                    user = common;
                    players.add(common);
                    commons.add(common);
                    i = 2;
                    while (i <= total_commoners) {
                        player_allotted++;
                        common = new Common(allot_id.remove(0));
                        players.add(common);
                        commons.add(common);
                        i++;
                    }
                    type = 4;
                    break;
                case 5:
                    Random rand = new Random();
                    int rand_choice = rand.nextInt(4) + 1;
                    switch (rand_choice) {
                        case 1:
                            player_allotted++;
                            Mafia mafia2 = new Mafia(allot_id.remove(0));
                            user = mafia2;
                            players.add(mafia2);
                            mafias.add(mafia2);
                            i = 2;
                            while (i <= total_mafias) {
                                player_allotted++;
                                mafia2 = new Mafia(allot_id.remove(0));
                                players.add(mafia2);
                                mafias.add(mafia2);
                                i++;
                            }
                            type = 1;
                            break;
                        case 2:
                            player_allotted++;
                            Detective detective2 = new Detective(allot_id.remove(0));
                            user = detective2;
                            players.add(detective2);
                            detectives.add(detective2);
                            i = 2;
                            while (i <= total_detectives) {
                                player_allotted++;
                                detective2 = new Detective(allot_id.remove(0));
                                players.add(detective2);
                                detectives.add(detective2);
                                i++;
                            }
                            type = 2;
                            break;
                        case 3:
                            player_allotted++;
                            Healer healer2 = new Healer(allot_id.remove(0));
                            user = healer2;
                            players.add(healer2);
                            healers.add(healer2);
                            i = 2;
                            while (i <= total_healers) {
                                player_allotted++;
                                healer2 = new Healer(allot_id.remove(0));
                                players.add(healer2);
                                healers.add(healer2);
                                i++;
                            }
                            type = 3;
                            break;
                        case 4:
                            player_allotted++;
                            Common common2 = new Common(allot_id.remove(0));
                            user = common2;
                            players.add(common2);
                            commons.add(common2);
                            i = 2;
                            while (i <= total_commoners) {
                                player_allotted++;
                                common2 = new Common(allot_id.remove(0));
                                players.add(common2);
                                commons.add(common2);
                                i++;
                            }
                            type = 4;
                            break;
                    }
                    break;
                default:
                    System.out.println("Please enter correct choice.");
                    correct_choice = false;
            }
        }
        if(type!=1)
        {
            i=1;
            while(i<=total_mafias) {
                player_allotted++;
                Mafia mafia=new Mafia(allot_id.remove(0));
                players.add(mafia);
                mafias.add(mafia);
                i++;
            }
        }
        if(type!=2)
        {
            i=1;
            while(i<=total_detectives) {
                player_allotted++;
                Detective detective=new Detective(allot_id.remove(0));
                players.add(detective);
                detectives.add(detective);
                i++;
            }
        }
        if(type!=3)
        {
            i=1;
            while(i<=total_healers) {
                player_allotted++;
                Healer healer=new Healer(allot_id.remove(0));
                players.add(healer);
                healers.add(healer);
                i++;
            }
        }
        if(type!=4)
        {
            i=1;
            while(i<=total_commoners) {
                player_allotted++;
                Common common=new Common(allot_id.remove(0));
                players.add(common);
                commons.add(common);
                i++;
            }
        }
        Collections.sort(players,new sort_by_id());     //sorting according to id
        Collections.sort(mafias,new sort_by_id());
        Collections.sort(detectives,new sort_by_id());
        Collections.sort(healers,new sort_by_id());
        Collections.sort(commons,new sort_by_id());
        System.out.print("You are player "+Integer.toString(user.getId())+"..\n");
        System.out.print("You are a ");
        if(type==1)
        {
            System.out.print("mafia\nOthers are: ");
            for(i=0;i< mafias.size();i++)
            {
                if(!mafias.get(i).equals(user))
                System.out.print("Player_"+Integer.toString(mafias.get(i).getId())+" ");
            }
            if(mafias.size()==1)
                System.out.print("none");
        }
        else if(type==2)
        {
            System.out.print("detective\nOthers are: ");
            for(i=0;i< detectives.size();i++)
            {
                if(!detectives.get(i).equals(user))
                    System.out.print("Player_"+Integer.toString(detectives.get(i).getId())+" ");
            }
            if(detectives.size()==1)
                System.out.print("none");
        }
        else if(type==3) {
            System.out.print("healer\nOthers are: ");
            for(i=0;i< healers.size();i++)
            {
                if(!healers.get(i).equals(user))
                    System.out.print("Player_"+Integer.toString(healers.get(i).getId())+" ");
            }
            if(healers.size()==1)
                System.out.print("none");
        }
        else if(type==4)
        {
            System.out.print("commoner");
        }
        for(int j=0;j<mafias.size();j++)
        {
            mafias.get(j).set_common_list(mafias);
        }
        for(int j=0;j<detectives.size();j++)
        {
            detectives.get(j).set_common_list(detectives);
        }
        for(int j=0;j<healers.size();j++)
        {
            healers.get(j).set_common_list(healers);
        }
    }

    private int lets_vote()                 //for voting.. returns id of voted out player
    {
        int voters=players.size();
        boolean mafia_selected=false;
        int z=0;
        int max_voted_against = 0;
        while(!mafia_selected && z<8) {
            mafia_selected=true;
            int vote_list[] = new int[voters];
            int choose = 0;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).equals(user)) {
                    boolean chosen_player = false;
                    while (!chosen_player) {
                        System.out.print("\nEnter player to vote out: ");
                        choose = in.nextInt();
                        for (int j = 0; j < players.size(); j++) {
                            if (choose == players.get(j).getId()) {
                                choose=j;
                                chosen_player = true;
                            }
                        }
                        if (!chosen_player) {
                            System.out.println("No such player/Player already killed..");
                        }
                    }
                } else {
                    choose = players.get(i).give_vote(players);
                }
                vote_list[choose]++;
            }
            max_voted_against=0;
            for (int j = 0; j < players.size(); j++) {
                if (vote_list[j] > vote_list[max_voted_against]) {
                    max_voted_against = j;
                }
            }
            int count = 0;
            for (int j = 0; j < players.size(); j++) {
                if (vote_list[j] == vote_list[max_voted_against]) {
                    count++;
                }
            }
            if (count > 1) {
                mafia_selected=false;
                System.out.print("\nAll Re-vote please.. No majority against anyone....");
                z++;
            }
        }
        if(z>=7)        // I allow all players to vote 7 times. If they still cant decide a player to vote out then system itself votes a player out...
        {
            System.out.println("Too much discussion. Kindly leave this task on the system... for we don't have ALL DAY -_+..");
            Random rand = new Random();
            max_voted_against=rand.nextInt(players.size());
        }
        return players.get(max_voted_against).getId();
    }

    private int check_dead(int target)                                 // for checking if mafia killed someone.. returns -1 if no one died..
    {
        for(int i=0;i<players.size();i++) {
            if (players.get(i).getId() == target) {
                if (players.get(i).getHP() == 0)
                    return 0;
                else
                    return -1;
            }
        }
        return -1;
    }
    private void kickout_or_kill_target(int id)   // to remove dead people from list by id..
    {
        int target=-1;
        boolean user_killed=false;
        if(user.getId()==id)
        {
            System.out.print("\nxx-->> User was killed <<----xx\n Enter some integer to continue: ");
            int enter_val=in.nextInt();
        }
        for(int i=0;i<players.size();i++)
        {
            if(id==players.get(i).getId())
                target=i;
        }
        if(players.get(target) instanceof Detective)
        {
            for(int i=0;i<detectives.size();i++)
            {
                if(detectives.get(i).equals(players.get(target)))
                {
                    detectives.remove(i);
                    total_detectives--;
                }
            }
        }
        else if(players.get(target) instanceof Mafia)
        {
            for(int i=0;i<mafias.size();i++)
            {
                if(mafias.get(i).equals(players.get(target)))
                {
                    mafias.remove(i);
                    total_mafias--;
                }
            }
        }
        else if(players.get(target) instanceof Healer)
        {
            for(int i=0;i<healers.size();i++)
            {
                if(healers.get(i).equals(players.get(target)))
                {
                    healers.remove(i);
                    total_healers--;
                }
            }
        }
        else if(players.get(target) instanceof Common)
        {
            for(int i=0;i<commons.size();i++)
            {
                if(commons.get(i).equals(players.get(target)))
                {
                    commons.remove(i);
                    total_commoners--;
                }
            }
        }
        removed_players.add(players.remove(target));
    }
    private void play_round()               // to play a round
    {
        int mafia_ID=-1;
        int healed;
        int target=-1;
        if(mafias.size()>0)
            target=mafias.get(0).take_turn(user,players);    // target pos in list
        if(detectives.size()>0)
            mafia_ID=detectives.get(0).take_turn(user,players);  //-1 if not Mafia else mafia id
        if(healers.size()>0)
            healed=healers.get(0).take_turn(user,players); // 0 for successfully increasing HP
        System.out.print("\n--- End of Action ----");
        int dead_or_not=check_dead(target);
        if(dead_or_not==-1)
        {
            System.out.print("\nNobody died...");
        }
        else
        {
            System.out.print("\nPlayer "+Integer.toString(target)+" has died...");
            kickout_or_kill_target(target);
        }
        if(mafia_ID==-1)
        {
            mafia_ID=lets_vote();                //
            System.out.print("\nPlayer "+Integer.toString(mafia_ID)+" has been voted out");
        }
        else
        {
            System.out.print("\nPlayer "+Integer.toString(mafia_ID)+" was tested successfully as Mafia.. So Killing him....");
        }
        kickout_or_kill_target(mafia_ID);
    }
    public void lets_begin()           //game begins here..
    {
        allot_players();
        System.out.print("\n\n-->>>  So here we begin... Input any integer and press enter to start...  ->> ");
        in.nextInt();
        int rounds=0;
        while(total_mafias!=0 && total_mafias<(total_commoners+total_detectives+total_healers)) //winning condition
        {
            rounds++;
            System.out.print("\n\n-->>>  Round "+Integer.toString(rounds)+"\n");
            System.out.print(players.size());
            System.out.println(" players are remaining: ");
            for(int i=0;i<players.size();i++)
            {
                System.out.print("Player_"+Integer.toString(players.get(i).getId())+" ");
            }
            //show();
            play_round();
            System.out.print("\n\n<<-- Round "+Integer.toString(rounds)+" ends...");
        }

        System.out.print("\n\n-->>> Game over....");
        if(total_mafias!=0)
        {
            System.out.print("\nThe Mafias have won!!");
        }
        else
        {
            System.out.print("\nThe Mafias have lost!!");
        }
        display();
    }
    public void display()   // final display function...
    {
        for(int i=0;i<removed_players.size();i++)
        {
            if(removed_players.get(i) instanceof Mafia)
                mafias.add((Mafia)removed_players.get(i));
            else if(removed_players.get(i) instanceof Detective)
                detectives.add((Detective)removed_players.get(i));
            else if(removed_players.get(i) instanceof Healer)
                healers.add((Healer) removed_players.get(i));
            else
                commons.add((Common)removed_players.get(i));
        }
        Collections.sort(mafias,new sort_by_id());         // again for sorting by id...
        Collections.sort(detectives,new sort_by_id());
        Collections.sort(healers,new sort_by_id());
        Collections.sort(commons,new sort_by_id());
        System.out.print("\nPlayers: ");
        for(int i=0;i<mafias.size();i++)
        {
            if(!mafias.get(i).equals(user))
                System.out.print(Integer.toString(mafias.get(i).getId())+" ");
            else
                System.out.print(Integer.toString(mafias.get(i).getId())+"[User] ");

        }
        System.out.print("were Mafias..\nPlayers: ");
        for(int i=0;i<detectives.size();i++)
        {
            if(!detectives.get(i).equals(user))
                System.out.print(Integer.toString(detectives.get(i).getId())+" ");
            else
                System.out.print(Integer.toString(detectives.get(i).getId())+"[User] ");
        }
        System.out.print("were Detectives..\nPlayers: ");
        for(int i=0;i<healers.size();i++)
        {
            if(!healers.get(i).equals(user))
                System.out.print(Integer.toString(healers.get(i).getId())+" ");
            else
                System.out.print(Integer.toString(healers.get(i).getId())+"[User] ");
        }
        System.out.print("were Healers..\nPlayers: ");
        for(int i=0;i<commons.size();i++)
        {
            if(!commons.get(i).equals(user))
                 System.out.print(Integer.toString(commons.get(i).getId())+" ");
            else
                System.out.print(Integer.toString(commons.get(i).getId())+"[User] ");
        }
        System.out.println("were Commoners..");
    }
    private void show()     // for showing alive players with their class and hp
    {
        for(int i=0;i<players.size();i++)
        {
            System.out.print("\nPlayer "+Integer.toString(players.get(i).getId())+" "+players.get(i).getClass()+" "+Float.toString(players.get(i).getHP()));
        }
    }
}


public class game_menu {
    public static void main(String[] args)     //main function.....
    {
        Scanner in= new Scanner(System.in);
        int total_players=0;
        System.out.println("\n\n---->> Welcome to MAFIA. (powered by UseLesS Engine) <<-------- ");
        while(total_players<6) {
            System.out.print("\nPlease enter the number of players (>=6): ");
            total_players = in.nextInt();
            if(total_players<6)
                System.out.println("--> We need atleast 6 players...");
        }
        Mafia_the_party_game game=new Mafia_the_party_game(total_players);
        game.lets_begin();
    }
}
