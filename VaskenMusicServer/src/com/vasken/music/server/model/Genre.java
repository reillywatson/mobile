package com.vasken.music.server.model;

/**
20		Alternative
29		Anime
2		Blues
4		Children's Music
5		Classical
3		Comedy
6		Country
17		Dance
50000063		Disney
25		Easy Listening
7		Electronic
28		Enka
50		Fitness &amp; Workout
50000064		French Pop
50000068		German Folk
50000066		German Pop
18		Hip Hop/Rap
8		Holiday
22		Inspirational
53		Instrumental
27		J-Pop
11		Jazz
51		K-Pop
52		Karaoke
30		Kayokyoku
12		Latin
13		New Age
9		Opera
1131		Pop Adult Contemporary
1132		Britpop 
1133		Pop/Rock
1134		Soft Rock
1135		Teen pop
15		R&B/Soul
24		Reggae
21		Rock
10		Singer/Songwriter
16		Soundtrack
50000061		Spoken Word
23		Vocal
19		World
*/
public enum Genre {
	Alternative(20, "Alternative"),
	Anime(29, "Anime"),
	Blues(2, "Blues"),
	ChildrensMusic(4, "Childrens Music"),
	Classical(5, "Classical"),
	Comedy(3, "Comedy"),
	Country(6, "Country"),
	Dance(17, "Dance"),
	Disney(50000063, "Disney"),
	EasyListening(25, "Easy Listening"),
	Electronic(7, "Electronic"),
	Enka(28, "Enka"),
	Fitness_Workout(50, "Fitness&Workout"),
	FrenchPop(50000064, "FrenchPop"),
	GermanFolk(50000068, "GermanFolk"),
	GermanPop(50000066, "GermanPop"),
	HipHop_Rap(18, "Hip-Hop/Rap"),
	Holiday(8, "Holiday"),
	Inspirational(22, "Inspirational"),
	Instrumental(53, "Instrumental"),
	J_Pop(27, "J-Pop"),
	Jazz(11, "Jazz"),
	K_Pop(51, "K-Pop"),
	Karaoke(52, "Karaoke"),
	Kayokyoku(30, "Kayokyoku"),
	Latin(12, "Latin"),
	NewAge(13, "New Age"),
	Opera(9, "Opera"),
	PopAdultContemporary(1131, "Pop - Adult Contemporary"),
	Britpop(1132, "Britpop"),
	Pop_Rock(1133, "Pop Rock"),
	SoftRock(1134, "Soft Rock"),
	TeenPop(1135, "Teen Pop"),
	RB_Soul(15, "R&B/Soul"),
	Reggae(24, "Reggae"),
	Rock(21, "Rock"),
	Singer_Songwriter(10, "Singer Songwriter"),
	Soundtrack(16, "Soundtrack"),
	SpokenWord(50000061, "Spoken Word"),
	Vocal(23, "Vocal"),
	World(19, "World");
	
	private int code;
	private String title;
	private Genre(int code, String title) {
		this.code = code;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	 public int getCode() {
		 return code;
	}
}
