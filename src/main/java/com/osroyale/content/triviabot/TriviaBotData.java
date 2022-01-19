package com.osroyale.content.triviabot;

/**
 * Holds all the trivia bot data.
 *
 * @author Daniel
 */
public enum TriviaBotData {
//    _1("Who is the owner of RunePK?", "Daniel"),
//    _2("Name one game developer.", "Daniel", "Michael", "Chex"),
    _3("What is the maximum combat level?", "126"),
    _4("What is the highest tier of donator?", "king donator", "king"),
    _5("What is the lowest tier of donator?", "regular donator", "regular"),
    _6("What is the maximum amount of friends allowed?", "200", "two hundred", "two-hundred"),
    _7("What item can be rewarded from the Fight caves activity?", "fire cape", "f cape"),
    _8("How many members can be in a single clan channel?", "80"),
    _9("What is the maximum level of any skill?", "99", "ninety nine", "ninety-nine"),
    _10("What is the URL of our forums?", "runepk.io", "www.runepk.io", "http://www.runepk.io"),
    _11("What server is dope af?", "runepk"),
    _12("What is the location name of home?", "edgeville"),
    _13("Name one of the rare drops that General Gaardoor drops.", "bandos tasset", "bandos boots", "bandos chestplate", "bandos hilt", "godsword shard"),
    _14("How many Hitpoints do Sharks heal?", "20", "twenty"),
    _15("What Magic level is needed to cast Ice Barrage", "94"),
    _16("What is the highest tier defender?", "dragon", "dragon defender"),
    _17("What is the required Attack level needed to wield the Abyssal whip?", "70", "seventy"),
    _18("What is the required Strength level needed to wield the Tzhaar-ket-om?", "60", "sixty"),
    _19("What is the required Ranged level needed to wield Red chinchompas?", "55", "fifty-five"),
    _20("What staff provides unlimited amount of earth and water runes?", "mystic mud staff", "mud", "mud battlestaff"),
    _21("What is the required Slayer level needed to kill skeletal Wyverns?", "72", "seventy-two"),
    _22("How many waves are there in the Fight Caves activity?", "15"),
    _23("How many charges can an amulet of glory hold?", "6", "six"),
    _24("What herb is required to make a Prayer potion?", "ranarr"),
    _25("What cmb lvl is required to join the veteran boat for the PC?", "100"),
    _26("How many waves are there in the Barbarian Assault activity?", "10", "ten"),
    _27("How many Hitpoints does the Corporeal Beast have?", "2000", "two-thousand"),
    _28("What boss drops the Elysian sigil?", "corporeal beast"),
    _29("How many commendation points does the Void knight mace cost? ", "250"),
    _30("What level is Zulrah?", "725"),
    _31("Name 1 rune required to cast the tele-block spell.", "chaos", "law", "death"),
    _32("How many Nature runes are needed to cast the Entangle spell?", "4"),
    _33("The Amulet of fury is created using which gem?", "onyx"),
    _34("What Ranged level is required to wield the Toxic blowpipe?", "75", "seventy-five"),
    _35("What Attack level is required to wield the Armadyl godsword?", "75", "seventy-five"),
    _36("How much ranged bonus does the Armadyl chestplate provide?", "33", "thirty-three"),
    _37("How many types of godswords are there?", "4", "four"),
    _38("How many Marks of grace are needed to purchace the full graceful set?", "260"),
    _39("What logs can be cut at level 90 Woodcutting?", "redwood", "redwood logs"),
    _40("What is the Prayer requirement needed to wield the Elysian spirit shield?", "75", "seventy-five"),
    _41("What ore can be mined at level 70 Mining?", "adamantite", "adamantite ore", "adamamant ore"),
    _42("Which godsword has the strongest special attack in PvP?", "ags", "armadyl", "armadyl godsword"),
    _43("Which godswords special attack can heal you?", "sgs", "saradomin", "saradomin godsword"),
    _44("Which godswords special attack drains your opponents stats?", "bgs", "bandos", "bandos godsword"),
    _45("Which godswords special attack freezes your enemy?", "zgs", "zamorak", "zamorak godsword"),
    _46("Which spirit shield is best for magic?", "arcane", "arcane spirit shield"),
    _47("Name one of the Barrows brothers.", "dharok", "torag", "karil", "guthan", "verac", "ahrim"),
    _48("How long does a full teleblock last for? [ In minutes ].", "5", "five"),
    _49("What level Wilderness can you teleport from with a glory amulet?", "30", "thirty"),
    _50("How many Hitpoints do Dark crabs heal?", "22", "twenty two"),
    _51("What herb is required to make a Super restore potion?", "snapdragon"),
    _52("On which continent is the Woodcutting guild located?", "zeah"),
    _53("What ore can be mined at level 55 Mining?", "mithril", "mithril ore"),
    _54("What logs can be cut at level 60 Woodcutting?", "yew", "yew logs"),
    _55("What tree requires level 35 Woodcutting to chop?", "teak", "teak tree"),
    _56("A burgundy colored rock contains what ore?", "iron", "iron ore"),
    _57("A black colored rock contains what mineral?", "coal"),
    _58("Which combat style does General Graardor use most often?", "melee"),
    _59("How many combat styles does Zulrah use?", "3", "three"),
    _60("How many words are in this question?", "7", "seven"),
    _61("What Hunter level is required to catch Black chinchompas?", "73", "seventy three"),
    _62("How many bankers are there in Edgeville bank?", "3", "three"),
    _63("The Dragon claws' special attack deals how many hitsplats?", "4", "four"),
    _64("What Defence level is required to equip Bandos armour?", "65", "sixty five"),
    _65("What Crafting level is required to cut Onyx gems?", "90", "ninety"),
    _66("Steel bars are smelted using Coal and which ore?", "iron", "iron ore"),
    _67("How many Hitpoints do Sand crabs have?", "60", "sixty"),
    _68("Planks made from which log are used at high Construction levels?", "mahogany"),
    _69("What is the exact amount of experience needed for level 99 in a skill?", "13034431"),
    _70("How many times can a skill be prestiged?", "4", "four"),
    _71("What special attack % does the Dragon dagger special use?", "25", "25%"),
    _72("What is the max damage of a single hit of the Dark bow's special attack?", "48", "fourty eight"),
    _73("What hunter level is required to catch black chinchompas?", "63", "sixty three"),
    _74("Which NPC sells skillcapes?", "Wise Old Man", "wise old man", "Wise old man"),
    _75("What is the entrance fee for brimhaven's agility arena?", "200", "200gp", "200 gp", "200 coins"),
    _76("Free players can use up to what type of arrows?", "adamant"),
    _77("What canoe can be made at level fifty-seven woodcutting?", "waka", "waka canoe"),
    _78("What farming level do you need to be to farm watermelon?", "47", "forty seven"),
    _79("When you bury big bones, how much prayer experience do you get?", "15", "fifteen"),
    _80("What city turns men and women into werewolves after being attacked?", "canifis"),
    _90("What item prevents citizens from canifis from turning into a werewolf?", "wolfbane", "wolfbane dagger", "a wolfbane", "a wolfbane dagger"),
    _91("In the RFD quest, what is the name of the enemy that the Gypsy freezes?", "culinaromancer"),
    _92("What magic level is required to cast wind wave?", "62", "sixty two"),
    _93("The skull sceptre gives the wearer how many teleports to barbarian village?", "five", "5"),
    _94("Which island do you sail to during the quest dragon slayer?", "crandor"),
    _95("Where is the magic guild located?", "yanille"),
    _96("What level is the dragon in dragon slayer?", "83", "eighty three"),
    _97("What is the fishing pet called?", "heron"),
    _98("What level are ducks?", "1", "one"),
    _99("What is the name of the dragon in dragon slayer?", "elvarg"),
    _100("Varrock lies in which kingdom?", "misthalin"),
    _101("What defence level do you need to be able to wear proselyte armor?", "30", "thirty"),
    _102("What woodcutting level do you need to chop the artic pine?", "54", "fifty four"),
    _103("What does H.A.M stand for?", "humans against monsters"),
    _104("In this skill, you can track and capture animals. What skill would this describe?", "hunter"),
    _105("What level are Jogres?", "53", "fifty three"),
    _106("What Slayer level do you need to be able to kill a Killerwatt?", "37", "thirty seven"),
    _107("What item is used to make bow strings?", "flax"),
    _108("How many Coal ores do you need to smelt a Mithril bar?", "4", "four"),
    _109("What Ranging Level do you need to be able to wear a Robin Hat?", "40", "forty"),
    _110("How many bars do you need to smith a Platebody of any kind?", "five", "5"),
    _111("What metal is made from 1 iron ore and 2 coal?", "steel"),
    _112("What Cooking Level do you need to be able to cook a Monkfish?", "62", "sixty two"),
    _113("What color is the party hat the Wise Old Man of Draynor Village is wearing?", "blue"),
    _114("What is the Varrock king's name?", "King Roald"),
//    _115("What fairy ring code is needed to get the Star Flower in the quest, 'Fairy Tale Part 2'?", "ckp"),
    _116("Which herb is required to make agility potions?", "toadflax"),
    _117("What mining level must you be to enter the mining guild?", "60", "sixty"),
    _118("Vampire Slayer: What level is the vampire (Count Draynor)?", "34", "thirty four"),
    _119("What magic level do you need to cast ice plateau teleport?", "89", "eighty nine"),;

    /** The trivial question. */
    private final String question;

    /** The trivial answer. */
    private final String[] answers;

    /** Constructs a new <code>TriviaBotData</code>. */
    TriviaBotData(String question, String... answers) {
        this.question = question;
        this.answers = answers;
    }

    /** Gets the trivial question. */
    public String getQuestion() {
        return question;
    }

    /** Gets the trivial answer. */
    public String[] getAnswers() {
        return answers;
    }
}
