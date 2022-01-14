# word-cloud-generator
My second Java coding assignment as an SD student at GMIT, handed in in January 2022.

## MAIN FEATURES
	
This app is a command line menu-driven Java application capable of parsing a set of files and URLs to generate a PNG file with a word-cloud displaying the most prominent words in decreasing font size, and in alternating styles and colours.

- Users can add local files or URLs from the main menu, adjust the settings to their liking, and choose option 10 to render.

- Frequently occurring, non-relevant words listed in the text file "ignorewords.txt" are not counted.

- Any HTML codes and replacement characters, numbers, and punctuation are discarded.

- Users can change any environmental variables via the menu, except for the location of the "./ignorewords.txt" as it was explicitly stated in the specs doc that it's just left there. Note that this means the app will always look for such a txt file in the working directory, not the location of the JAR.

## ADDITIONAL FEATURES
	
### 1. PARSER

- This app is multi-threaded and allows users to add multiple files and URLs to be parsed which are then processed simultaneously.

- The app converts relative paths to absolute paths and doesn't accept the same file or URL twice in the same list.

- In addition to stop words and tags, English contractions like "'ll", "'s", or "'d", are also discarded.

### 2. CLOUD

- Users can change the size of the image.

- Font sizes are calculated rather than randomised, using a formula based on various parameters to yield a reasonably legible result.

- Colours are also calculated rather than randomised for a more attractive result.

- The words may overlap, however, they won't go over the edges of the image except potentially when using very thin and long formats.

### 3. APP

- All parser and renderer settings have reasonable lower and upper limits to guide users without restricting them.

- Because this app relies heavily on user input of unique identifiers, strict validation has been implemented via an interface and both specification and specialisation inheritance to increase stability.

- Any user-facing strings have been organised in an Enum class to keep the code clean.


## DESIGN NOTES

### Validation
A set of classes that act as "getters" for various type instances from the user via the console. The prompt loops until users enter a correct URL, correct file path etc. Objects must pass a custom logic check (validation) to make sure they are suitable for the task at hand. For example, one could check if an HTTP request to a URL returns a certain response code instead of just instantiating the object without knowing anything about it. This way, there is less potential for exceptions further down the line.
Users can break the loop by passing a blank line.

The Validation.result method returns the validated object, if it has passed the validation, or else, null. It is meant to be used as a method parameter: if the passed value is not null, it has already been validated and should be safe to work with.
The interface could also be used to work with other input types - even random values or input streams with scraped miscellanea from the web - perhaps as a thread that's looking for a certain type of object that meets certain criteria among a plethora of other stuff and collects them in a list - but I designed it with the console in mind.

The point is: When we need a reliable type instance from an unreliable source, implementing Validation is safer than fiddling with try-catch.

### FrequencyTabulation
This was approached as a three-step process: read a text line by line, read each line word by word, and do a tally. The interface describes this. Most of the functionality is already implemented in the abstract class. The part that's different for URLs, files and whatever else can yield an input stream, is instantiating the BufferedReader, so that's done in the derived concrete classes.

### Central column

In between the polymorphism to the left and right on the diagram, the application design has a central column of final classes with static variables that maintain instances and delegate behaviour.

- Runner includes the main method as well as the menu and anything to do with user interaction.

- Tabulators is essentially a job queue that creates and maintains instances of frequency tabulators, and its class variables determine the specifics of the parsing job - what words to skip etc.

- The word cloud renderer is fairly self-contained and only deals with rendering the image from the word list it receives. It has class variables to determine the specifics of the job, like image size and such. For extra encapsulation, it contains a private inner enum with fonts to cycle through.

- Anything to do with the image file as opposed to the image object - its path, the name and the actual writing - is done by WordCloudIO.

- I prefer keeping Strings separate from any logic to be able to reuse them, and to avoid long and messy code, and they're constants of a single type, so I put them into an enum.

## REFERENCES

This app uses code from external sources as commented in the respective files:

- The Runner.clearConsole method was taken from the web (now also works on Unix-based systems).

- A method to validate file names was inspired by a blog post.