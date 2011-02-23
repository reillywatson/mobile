# coding=utf-8
from pysqlite2 import dbapi2 as sqlite
import codecs
import re

def main():
	winners = '''The Informer
Mutiny on the Bounty
Mutiny on the Bounty
Black Fury
Mutiny on the Bounty
The Story of Louis Pasteur
Mr. Deeds Goes to Town
Dodsworth
My Man Godfrey
San Francisco
Captains Courageous
Conquest
A Star Is Born
Night Must Fall
The Life of Emile Zola
Boys Town
Algiers
Angels with Dirty Faces
The Citadel
Pygmalion
Goodbye, Mr. Chips
Gone with the Wind
Wuthering Heights
Babes in Arms
Mr. Smith Goes to Washington
The Philadelphia Story
The Great Dictator
The Grapes of Wrath
Abe Lincoln in Illinois
Rebecca
Sergeant York
Penny Serenade
The Devil and Daniel Webster
Here Comes Mr. Jordan
Citizen Kane
Yankee Doodle Dandy
Random Harvest
The Pride of the Yankees
Mrs. Miniver
The Pied Piper
Watch on the Rhine
Casablanca
For Whom the Bell Tolls
Madame Curie
The Human Comedy
Going My Way
Gaslight
Going My Way
None but the Lonely Heart
Wilson
The Lost Weekend
The Bells of St. Mary's
Anchors Aweigh
The Keys of the Kingdom
A Song to Remember
The Best Years of Our Lives
Henry V
The Jolson Story
The Yearling
It's a Wonderful Life
A Double Life
Body and Soul
Gentleman's Agreement
Life with Father
Mourning Becomes Electra
Hamlet
Johnny Belinda
The Search
When My Baby Smiles at Me
Sitting Pretty
All the King's Men
Champion
Twelve O'Clock High
The Hasty Heart
Sands of Iwo Jima
Cyrano de Bergerac
The Magnificent Yankee
Sunset Boulevard
Harvey
Father of the Bride
The African Queen
A Streetcar Named Desire
A Place in the Sun
Bright Victory
Death of a Salesman
High Noon
Viva Zapata!
The Bad and the Beautiful
Moulin Rouge
The Lavender Hill Mob
Stalag 17
Julius Caesar
The Robe
From Here to Eternity
From Here to Eternity
On the Waterfront
The Caine Mutiny
The Country Girl
A Star Is Born
Adventures of Robinson Crusoe
Marty
Love Me or Leave Me
East of Eden
The Man with the Golden Arm
Bad Day at Black Rock
The King and I
Giant
Lust for Life
Giant
Richard III
The Bridge on the River Kwai
Sayonara
A Hatful of Rain
Witness for the Prosecution
Wild Is the Wind
Separate Tables
The Defiant Ones
Cat on a Hot Tin Roof
The Defiant Ones
The Old Man and the Sea
Ben-Hur
Room at the Top
Some Like It Hot
The Last Angry Man
Anatomy of a Murder
Elmer Gantry
Sons and Lovers
The Apartment
The Entertainer
Inherit the Wind
Judgment at Nuremberg
Fanny
The Hustler
Judgment at Nuremberg
The Mark
To Kill a Mockingbird
Birdman of Alcatraz
Days of Wine and Roses
Divorce, Italian Style
Lawrence of Arabia
Lilies of the Field
Tom Jones
This Sporting Life
Cleopatra
Hud
My Fair Lady
Becket
Becket
Zorba the Greek
Dr. Strangelove
Cat Ballou
The Spy Who Came in from the Cold
Othello
The Pawnbroker
Ship of Fools
A Man for All Seasons
The Russians Are Coming, the Russians Are Coming
Who's Afraid of Virginia Woolf?
Alfie
The Sand Pebbles
In the Heat of the Night
Bonnie and Clyde
The Graduate
Cool Hand Luke
Guess Who's Coming to Dinner
Charly
The Heart Is a Lonely Hunter
The Fixer
Oliver!
The Lion in Winter
True Grit
Anne of the Thousand Days
Midnight Cowboy
Goodbye, Mr. Chips
Midnight Cowboy
Patton
I Never Sang for My Father
The Great White Hope
Five Easy Pieces
Love Story
The French Connection
Sunday Bloody Sunday
Kotch
The Hospital
Fiddler on the Roof
The Godfather
Sleuth
Sleuth
The Ruling Class
Sounder
Save the Tiger
Last Tango in Paris
The Last Detail
Serpico
The Sting
Harry and Tonto
Murder on the Orient Express
Lenny
Chinatown
The Godfather Part II
One Flew Over the Cuckoo's Nest
The Sunshine Boys
Dog Day Afternoon
The Man in the Glass Booth
Give 'em Hell, Harry!
Network
Taxi Driver
Seven Beauties
Network
Rocky
The Goodbye Girl
Annie Hall
Equus
A Special Day
Saturday Night Fever
Coming Home
Heaven Can Wait
The Buddy Holly Story
The Deer Hunter
The Boys from Brazil
Kramer vs. Kramer
The China Syndrome
…And Justice for All
All That Jazz
Being There
Raging Bull
The Great Santini
The Elephant Man
Tribute
The Stunt Man
On Golden Pond
Reds
Atlantic City
Arthur
Absence of Malice
Gandhi
Tootsie
Missing
The Verdict
My Favorite Year
Tender Mercies
Educating Rita
Reuben, Reuben
The Dresser
The Dresser
Amadeus
Starman
Under the Volcano
Amadeus
The Killing Fields
Kiss of the Spider Woman
Witness
Murphy's Romance
Prizzi's Honor
Runaway Train
The Color of Money
Round Midnight
Mona Lisa
Children of a Lesser God
Salvador
Wall Street
Broadcast News
Dark Eyes
Ironweed
Good Morning, Vietnam
Rain Man
Mississippi Burning
Big
Stand and Deliver
Pelle the Conqueror
wis - My Left Foot
Henry V
Born on the Fourth of July
Driving Miss Daisy
Dead Poets Society
Reversal of Fortune
Dances with Wolves
Awakenings
Cyrano de Bergerac
The Field
The Silence of the Lambs
Bugsy
Cape Fear
The Prince of Tides
The Fisher King
Scent of a Woman
Chaplin
Unforgiven
The Crying Game
Malcolm X
Philadelphia
wis - In the Name of the Father
What's Love Got to Do with It
The Remains of the Day
Schindler's List
Forrest Gump
The Shawshank Redemption
The Madness of King George
Nobody's Fool
Pulp Fiction
Leaving Las Vegas
Mr. Holland's Opus
Nixon
Dead Man Walking
Il Postino
Shine
Jerry Maguire
The English Patient
The People vs. Larry Flynt
Sling Blade
As Good
Good Will Hunting
The Apostle
Ulee's Gold
Wag the Dog
Life Is Beautiful
Saving Private Ryan
Gods and Monsters
Affliction
American History X
American Beauty
The Insider
The Straight Story
Sweet and Lowdown
he Hurricane
Gladiator
Before Night Falls
Cast Away
Pollock
Quills
Training Day
A Beautiful Mind
I Am Sam
Ali
In the Bedroom
The Pianist
Adaptation.
The Quiet American
wis - Gangs of New York
About Schmidt
Mystic River
Pirates of the Caribbean: The Curse of the Black Pearl
House of Sand and Fog
Cold Mountain
Lost in Translation
Ray
Hotel Rwanda
Finding Neverland
The Aviator
Million Dollar Baby
Capote
Hustle & Flow
Brokeback Mountain
Walk the Line
Good Night, and Good Luck
The Last King of Scotland
Blood Diamond
Half Nelson
Venus
The Pursuit of Happyness
wis - There Will Be Blood
Michael Clayton
Sweeney Todd: The Demon Barber of Fleet Street
In the Valley of Elah
Eastern Promises
Milk
The Visitor
Frost/Nixon
The Curious Case of Benjamin Button
The Wrestler
Crazy Heart
Up in the Air
A Single Man
Invictus
The Hurt Locker
Dangerous
Escape Me Never
Private Worlds
Alice Adams
Becky Sharp
The Dark Angel
The Great Ziegfeld
Theodora Goes Wild
Valiant Is the Word for Carrie
My Man Godfrey
Romeo and Juliet
The Good Earth
The Awful Truth
Camille
A Star Is Born
Stella Dallas
Jezebel
White Banners
Pygmalion
Marie Antoinette
Three Comrades
Gone with the Wind
Dark Victory
Love Affair
Ninotchka
Goodbye, Mr. Chips
Kitty Foyle
The Letter
Rebecca
The Philadelphia Story
Our Town
Suspicion
The Little Foxes
Hold Back the Dawn
Blossoms in the Dust
Ball of Fire
Mrs. Miniver
Now, Voyager
Woman of the Year
My Sister Eileen
The Pride of the Yankees
The Song of Bernadette
The More the Merrier
For Whom the Bell Tolls
The Constant Nymph
Madame Curie
Gaslight
Since You Went Away
Mr. Skeffington
Mrs. Parkington
Double Indemnity
Mildred Pierce
The Bells of St. Mary's
The Valley of Decision
Love Letters
Leave Her to Heaven
To Each His Own
Brief Encounter
Duel in the Sun
Sister Kenny
The Yearling
The Farmer's Daughter
Possessed
Smash-Up, the Story of a Woman
Gentleman's Agreement
Mourning Becomes Electra
Johnny Belinda
Joan of Arc
The Snake Pit
I Remember Mama
Sorry, Wrong Number
The Heiress
Pinky
My Foolish Heart
Edward, My Son
Come to the Stable
Born Yesterday
All About Eve
All About Eve
Caged
Sunset Boulevard
A Streetcar Named Desire
The African Queen
Detective Story
A Place in the Sun
The Blue Veil
Come Back, Little Sheba
Sudden Fear
The Star
The Member of the Wedding
With a Song in My Heart
Roman Holiday
Lili
Mogambo
From Here to Eternity
The Moon Is Blue
The Country Girl
Carmen Jones
A Star Is Born
Sabrina
Magnificent Obsession
The Rose Tattoo
I'll Cry Tomorrow
Summertime
Love Is a Many-Splendored Thing
Interrupted Melody
Anastasia
Baby Doll
The Rainmaker
The Bad Seed
The King and I
The Three Faces of Eve
Heaven Knows, Mr. Allison
Wild Is the Wind
Raintree County
Peyton Place
I Want to Live!
Separate Tables
Some Came Running
Auntie Mame
Cat on a Hot Tin Roof
Room at the Top
Pillow Talk
The Nun's Story
Suddenly, Last Summer
Suddenly, Last Summer
BUtterfield 8
Sunrise at Campobello
The Sundowners
The Apartment
Never on Sunday
Two Women
Breakfast at Tiffany's
The Hustler
Summer and Smoke
Splendor in the Grass
The Miracle Worker
What Ever Happened to Baby Jane?
Long Day's Journey Into Night
Sweet Bird of Youth
Days of Wine and Roses
Hud
The L-Shaped Room
Irma la Douce
This Sporting Life
Love with the Proper Stranger
Mary Poppins
The Pumpkin Eater
Marriage Italian-Style
The Unsinkable Molly Brown
Séance on a Wet Afternoon
Darling
The Sound of Music
The Collector
A Patch of Blue
Ship of Fools
Who's Afraid of Virginia Woolf?
A Man and a Woman
The Shop on Main Street
Georgy Girl
Morgan!
Guess Who's Coming to Dinner
The Graduate
Bonnie and Clyde
The Whisperers
Wait Until Dark
The Prime of Miss Jean Brodie
Anne of the Thousand Days
They Shoot Horses, Don't They?
The Sterile Cuckoo
The Happy Ending
Women in Love
The Great White Hope
Love Story
Ryan's Daughter
Diary of a Mad Housewife
Klute
McCabe & Mrs. Miller
Sunday Bloody Sunday
Mary, Queen of Scots
Nicholas and Alexandra
Cabaret
Lady Sings the Blues
Travels with My Aunt
Sounder
The Emigrants
A Touch of Class
The Exorcist
Cinderella Liberty
The Way We Were
Summer Wishes, Winter Dreams
Alice Doesn't Live Here Anymore
Claudine
Chinatown
Lenny
A Woman Under the Influence
One Flew Over the Cuckoo's Nest
The Story of Adele H.
rgret - Tommy
Hedda
Hester Street
Network
ristine Barrault - Cousin, cousine
Rocky
Carrie
Face to Face
Annie Hall
The Turning Point
Julia
The Turning Point
The Goodbye Girl
Coming Home
Autumn Sonata
Same Time, Next Year
An Unmarried Woman
Interiors
Norma Rae
Starting Over
The China Syndrome
Chapter Two
The Rose
Coal Miner's Daughter
Resurrection
Private Benjamin
Ordinary People
Gloria
On Golden Pond
Reds
Only When I Laugh
Atlantic City
The French Lieutenant's Woman
Sophie's Choice
Victor Victoria
Frances
Missing
An Officer and a Gentleman
Terms of Endearment
Testament
Silkwood
Educating Rita
Terms of Endearment
Places in the Heart
A Passage to India
Country
The Bostonians
The River
The Trip to Bountiful
Agnes of God
The Color Purple
Sweet Dreams
Out of Africa
Children of a Lesser God
The Morning After
Crimes of the Heart
Peggy Sue Got Married
Aliens
Moonstruck
Fatal Attraction
Broadcast News
Anna
Ironweed
The Accused
Dangerous Liaisons
Working Girl
A Cry in the Dark
Gorillas in the Mist
Driving Miss Daisy
Camille Claudel
Shirley Valentine
Music Box
The Fabulous Baker Boys
Misery
The Grifters
Pretty Woman
Postcards from the Edge
Mr. and Mrs. Bridge
The Silence of the Lambs
Thelma & Louise
Rambling Rose
For the Boys
Thelma & Louise
Howards End
Indochine
Passion Fish
Love Field
Lorenzo's Oil
The Piano
What's Love Got to Do with It
Six Degrees of Separation
The Remains of the Day
Shadowlands
Blue Sky
Nell
Tom & Viv
Little Women
The Client
Dead Man Walking
Leaving Las Vegas
Casino
The Bridges of Madison County
Sense and Sensibility
Fargo
Secrets & Lies
Marvin's Room
The English Patient
Breaking the Waves
As Good
The Wings of the Dove
Afterglow
Mrs. Brown
Titanic
Shakespeare in Love
Elizabeth
Central Station
One True Thing
Hilary and Jackie
Boys Don't Cry
American Beauty
Tumbleweeds
The End of the Affair
Music of the Heart
Erin Brockovich
The Contender
Chocolat
Requiem for a Dream
You Can Count on Me
Monster's Ball
Iris
Moulin Rouge!
In the Bedroom
Bridget Jones's Diary
The Hours
Frida
Unfaithful
Far from Heaven
Chicago
Monster
Whale Rider
Something's Gotta Give
In America
21 Grams
Million Dollar Baby
Being Julia
Maria Full of Grace
Vera Drake
Eternal Sunshine of the Spotless Mind
Walk the Line
Mrs Henderson Presents
Transamerica
Pride & Prejudice
North Country
The Queen
Volver
Notes on a Scandal
The Devil Wears Prada
Little Children
La Vie en Rose
Elizabeth: The Golden Age
Away from Her
The Savages
Juno
The Reader
Rachel Getting Married
Changeling
Frozen River
Doubt
The Blind Side
The Last Station
An Education
Precious
Julie & Julia
Wings
The Racket
Seventh Heaven
The Broadway Melody
Alibi
The Hollywood Revue of 1929
In Old Arizona
The Patriot
All Quiet on the Western Front
The Big House
Disraeli
The Divorcee
The Love Parade
Cimarron
East Lynne
The Front Page
Skippy
Trader Horn
Grand Hotel
Arrowsmith
Bad Girl
The Champ
Five Star Final
One Hour with You
Shanghai Express
The Smiling Lieutenant
Cavalcade
42nd Street
A Farewell to Arms
I Am a Fugitive from a Chain Gang
Lady for a Day
Little Women
The Private Life of Henry VIII
She Done Him Wrong
Smilin' Through
State Fair
It Happened One Night
The Barretts of Wimpole Street
Cleopatra
Flirtation Walk
The Gay Divorcee
Here Comes the Navy
The House of Rothschild
Imitation of Life
One Night of Love
The Thin Man
Viva Villa!
The White Parade
Mutiny on the Bounty
Alice Adams
Broadway Melody of 1936
Captain Blood
David Copperfield
The Informer
The Lives of a Bengal Lancer
A Midsummer Night's Dream
Les Misérables
Naughty Marietta
Ruggles of Red Gap
Top Hat
The Great Ziegfeld
Anthony Adverse
Dodsworth
Libeled Lady
Mr. Deeds Goes to Town
Romeo and Juliet
San Francisco
The Story of Louis Pasteur
A Tale of Two Cities
Three Smart Girls
The Life of Emile Zola
The Awful Truth
Captains Courageous
Dead End
The Good Earth
In Old Chicago
Lost Horizon
One Hundred Men and a Girl
Stage Door
A Star Is Born
You Can't Take It With You
The Adventures of Robin Hood
Alexander's Ragtime Band
Boys Town
The Citadel
Four Daughters
Grand Illusion
Jezebel
Pygmalion
Test Pilot
Gone with the Wind
Dark Victory
Goodbye, Mr. Chips
Love Affair
Mr. Smith Goes to Washington
Ninotchka
Of Mice and Men
Stagecoach
The Wizard of Oz
Wuthering Heights
Rebecca
All This, and Heaven Too
Foreign Correspondent
The Grapes of Wrath
The Great Dictator
Kitty Foyle
The Letter
The Long Voyage Home
Our Town
The Philadelphia Story
How Green Was My Valley
Blossoms in the Dust
Citizen Kane
Here Comes Mr. Jordan
Hold Back the Dawn
The Little Foxes
The Maltese Falcon
One Foot in Heaven
Sergeant York
Suspicion
Mrs. Miniver
49th Parallel
Kings Row
The Magnificent Ambersons
The Pied Piper
The Pride of the Yankees
Random Harvest
The Talk of the Town
Wake Island
Yankee Doodle Dandy
Casablanca
For Whom the Bell Tolls
Heaven Can Wait
The Human Comedy
In Which We Serve
Madame Curie
The More the Merrier
The Ox-Bow Incident
The Song of Bernadette
Watch on the Rhine
Going My Way
Double Indemnity
Gaslight
Since You Went Away
Wilson
The Lost Weekend
Anchors Aweigh
The Bells of St. Mary's
Mildred Pierce
Spellbound
The Best Years of Our Lives
Henry V
It's a Wonderful Life
The Razor's Edge
The Yearling
Gentleman's Agreement
The Bishop's Wife
Crossfire
Great Expectations
Miracle on 34th Street
Hamlet
Johnny Belinda
The Red Shoes
The Snake Pit
The Treasure of the Sierra Madre
All the King's Men
Battleground
The Heiress
A Letter to Three Wives
Twelve O'Clock High
All About Eve
Born Yesterday
Father of the Bride
King Solomon's Mines
Sunset Boulevard
An American in Paris
Decision Before Dawn
A Place in the Sun
Quo Vadis
A Streetcar Named Desire
The Greatest Show on Earth
High Noon
Ivanhoe
Moulin Rouge
The Quiet Man
From Here to Eternity
Julius Caesar
The Robe
Roman Holiday
Shane
On the Waterfront
The Caine Mutiny
The Country Girl
Seven Brides for Seven Brothers
Three Coins in the Fountain
Marty
Love Is a Many-Splendored Thing
Mister Roberts
Picnic
The Rose Tattoo
Around the World in 80 Days
Friendly Persuasion
Giant
The King and I
The Ten Commandments
The Bridge on the River Kwai
Peyton Place
Sayonara
12 Angry Men
Witness for the Prosecution
Gigi
Auntie Mame
Cat on a Hot Tin Roof
The Defiant Ones
Separate Tables
Ben-Hur
Anatomy of a Murder
The Diary of Anne Frank
The Nun's Story
Room at the Top
The Apartment
The Alamo
Elmer Gantry
Sons and Lovers
The Sundowners
West Side Story
Fanny
The Guns of Navarone
The Hustler
Judgment at Nuremberg
Lawrence of Arabia
The Longest Day
The Music Man
Mutiny on the Bounty
To Kill a Mockingbird
Tom Jones
America, America
Cleopatra
How the West Was Won
Lilies of the Field
My Fair Lady
Becket
Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb
Mary Poppins
Zorba the Greek
The Sound of Music
Darling
Doctor Zhivago
Ship of Fools
A Thousand Clowns
A Man for All Seasons
Alfie
The Russians Are Coming, the Russians Are Coming
The Sand Pebbles
Who's Afraid of Virginia Woolf?
In the Heat of the Night
Bonnie and Clyde
Doctor Dolittle
The Graduate
Guess Who's Coming to Dinner
Oliver!
Funny Girl
The Lion in Winter
Rachel, Rachel
Romeo and Juliet
Midnight Cowboy
Anne of the Thousand Days
Butch Cassidy and the Sundance Kid
Hello, Dolly!
Z
Patton
Airport
Five Easy Pieces
Love Story
MASH
The French Connection
A Clockwork Orange
Fiddler on the Roof
The Last Picture Show
Nicholas and Alexandra
The Godfather
Cabaret
Deliverance
The Emigrants
Sounder
The Sting
American Graffiti
Cries and Whispers
The Exorcist
A Touch of Class
The Godfather Part II
Chinatown
The Conversation
Lenny
The Towering Inferno
One Flew Over the Cuckoo's Nest
Barry Lyndon
Dog Day Afternoon
Jaws
Nashville
Rocky
All the President's Men
Bound for Glory
Network
Taxi Driver
Annie Hall
The Goodbye Girl
Julia
Star Wars
The Turning Point
The Deer Hunter
Coming Home
Heaven Can Wait
Midnight Express
An Unmarried Woman
Kramer vs. Kramer
All That Jazz
Apocalypse Now
Breaking Away
Norma Rae
Ordinary People
Coal Miner's Daughter
The Elephant Man
Raging Bull
Tess
Chariots of Fire
Atlantic City
On Golden Pond
Raiders of the Lost Ark
Reds
Gandhi
E.T. the Extra-Terrestrial
Missing
Tootsie
The Verdict
Terms of Endearment
The Big Chill
The Dresser
The Right Stuff
Tender Mercies
Amadeus
The Killing Fields
A Passage to India
Places in the Heart
A Soldier's Story
Out of Africa
The Color Purple
Kiss of the Spider Woman
Prizzi's Honor
Witness
Platoon
Children of a Lesser God
Hannah and Her Sisters
The Mission
A Room with a View
The Last Emperor
Broadcast News
Fatal Attraction
Hope and Glory
Moonstruck
Rain Man
The Accidental Tourist
Dangerous Liaisons
Mississippi Burning
Working Girl
Driving Miss Daisy
Born on the Fourth of July
Dead Poets Society
Field of Dreams
My Left Foot
Dances with Wolves
Awakenings
Ghost
The Godfather Part III
Goodfellas
The Silence of the Lambs
Beauty and the Beast
Bugsy
JFK
The Prince of Tides
Unforgiven
The Crying Game
A Few Good Men
Howards End
Scent of a Woman
Schindler's List
The Fugitive
In the Name of the Father
The Piano
The Remains of the Day
Forrest Gump
Four Weddings and a Funeral
Pulp Fiction
Quiz Show
The Shawshank Redemption
Braveheart
Apollo 13
Babe
Il Postino
Sense and Sensibility
The English Patient
Fargo
Jerry Maguire
Secrets &amp; Lies
Shine
Titanic
As Good as It Gets
The Full Monty
Good Will Hunting
L.A. Confidential
Shakespeare in Love
Elizabeth
Life Is Beautiful
Saving Private Ryan
The Thin Red Line
American Beauty
The Cider House Rules
The Green Mile
The Insider
The Sixth Sense
Gladiator
Chocolat
Crouching Tiger, Hidden Dragon
Erin Brockovich
Traffic
A Beautiful Mind
Gosford Park
In the Bedroom
The Lord of the Rings: The Fellowship of the Ring
Moulin Rouge!
Chicago
Gangs of New York
The Hours
The Lord of the Rings: The Two Towers
The Pianist
The Lord of the Rings: The Return of the King
Lost in Translation
Master and Commander: The Far Side of the World
Mystic River
Seabiscuit
Million Dollar Baby
The Aviator
Finding Neverland
Ray
Sideways
Crash
Brokeback Mountain
Capote
Good Night, and Good Luck
Munich
The Departed
Babel
Letters from Iwo Jima
Little Miss Sunshine
The Queen
No Country for Old Men
Atonement
Juno
Michael Clayton
There Will Be Blood
Slumdog Millionaire
The Curious Case of Benjamin Button
Frost/Nixon
Milk
The Reader
The Hurt Locker
Avatar
The Blind Side
District 9
An Education
Inglourious Basterds
Precious: Based on the Novel "Push" by Sapphire
A Serious Man
Up
Up in the Air
The Lion in Winter
The Subject was Roses
Isadora
Rachel, Rachel
'''.lower().split('\n')
	con = sqlite.connect('../oscars.sqlite')
	file = codecs.open('moviequotes.txt', 'r', 'utf-8')
	lineNo = 0
	for line in file:
		lineNo = lineNo + 1
		if lineNo % 100 == 0:
			print 'processing line ' + str(lineNo)
		groups = line.split('<')
		author = [x.lstrip('title>') for x in groups if x.startswith('title>')]
		if len(author) == 1:
			author = author[0]
			if author.lower() in winners:
				quoteText = re.search('<text>(.*?)</text>', line).group(0).replace('<text>','').replace('</text>','')
				values = {'film':author, 'quote':quoteText}
				cur = con.cursor()
				cur.execute('insert into Quote(Film, Quote) values (:film, :quote)', values)
	con.commit()

if __name__ == '__main__':
	main()

