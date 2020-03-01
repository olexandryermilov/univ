import java.util.regex.Pattern

import scala.io.Source
import scala.util.Random

object ElizaScala {


  case class PhraseMatcher(regex: String, responses: Seq[String]) {
    def randomResponse(): String = responses(Random.nextInt(responses.size))

    def reflect(str: String): String = str.split(" ").map(word => reflections.getOrElse(word, word)).mkString(" ")

    def respond(text: String): String = {
      val m = matcherPattern.matcher(text)
      if (m.find()) {
        var p = randomResponse()
        val tmp = m.groupCount()
        for (i <- 0 until tmp) {
          val s = reflect(m.group(i + 1))
          p = p.replace("{" + i + "}", s)
        }
        p
      } else ""
    }

    lazy val matcherPattern: Pattern = Pattern.compile(regex)
  }

  val reflections: Map[String, String] = Map(
    "am" -> "are",
    "was" -> "were",
    "i" -> "you",
    "i'd" -> "you would",
    "i've" -> "you have",
    "my" -> "your",
    "are" -> "am",
    "you've" -> "I have",
    "you'll" -> "I will",
    "your" -> "my",
    "yours" -> "mine",
    "you" -> "I",
    "me" -> "you"
  )

  var phrases: Seq[PhraseMatcher] = Seq(
    PhraseMatcher("i need (.*)", Seq("Why do you need {0}?", "Would it really help you to get {0}?", "Are you sure you need {0}?")),
    PhraseMatcher("why don'?t you ([^\\?]*)\\??", Seq("Do you really think I don't {0}?", "Perhaps eventually I will {0}.", "Do you really want me to {0}?")),
    PhraseMatcher("why can'?t I ([^\\?]*)\\??", Seq("Do you think you should be able to {0}?", "If you could {0}, what would you do?", "I don't know -- why can't you {0}?", "Have you really tried?")),
    PhraseMatcher("i can'?t (.*)", Seq("How do you know you can't {0}?", "Perhaps you could {0} if you tried.", "What would it take for you to {0}?")),
    PhraseMatcher("i am (.*)", Seq("Did you come to me because you are {0}?", "How long have you been {0}?", "How do you feel about being {0}?")),
    PhraseMatcher("i'?m (.*)", Seq("How does being {0} make you feel?", "Do you enjoy being {0}?", "Why do you tell me you're {0}?", "Why do you think you're {0}?")),
    PhraseMatcher("are you ([^\\?]*)\\??", Seq("Why does it matter whether I am {0}?", "Would you prefer it if I were not {0}?", "Perhaps you believe I am {0}.", "I may be {0} -- what do you think?")),
    PhraseMatcher("what (.*)", Seq("Why do you ask?", "How would an answer to that help you?", "What do you think?")),
    PhraseMatcher("how (.*)", Seq("How do you suppose?", "Perhaps you can answer your own question.", "What is it you're really asking?")),
    PhraseMatcher("because (.*)", Seq("Is that the real reason?", "What other reasons come to mind?", "Does that reason apply to anything else?", "If {0}, what else must be true?")),
    PhraseMatcher("(.*) sorry (.*)", Seq("There are many times when no apology is needed.", "What feelings do you have when you apologize?")),
    PhraseMatcher("hello(.*)", Seq("Hello... I'm glad you could drop by today.", "Hi there... how are you today?", "Hello, how are you feeling today?")),
    PhraseMatcher("i think (.*)", Seq("Do you doubt {0}?", "Do you really think so?", "But you're not sure {0}?")),
    PhraseMatcher("(.*) friend (.*)", Seq("Tell me more about your friends.", "When you think of a friend, what comes to mind?", "Why don't you tell me about a childhood friend?")),
    PhraseMatcher("yes", Seq("You seem quite sure.", "OK, but can you elaborate a bit?")),
    PhraseMatcher("(.*) computer(.*)", Seq("Are you really talking about me?", "Does it seem strange to talk to a computer?", "How do computers make you feel?", "Do you feel threatened by computers?")),
    PhraseMatcher("is it (.*)", Seq("Do you think it is {0}?", "Perhaps it's {0} -- what do you think?", "If it were {0}, what would you do?", "It could well be that {0}.")),
    PhraseMatcher("can you ([^\\?]*)\\??", Seq("What makes you think I can't {0}?", "If I could {0}, then what?", "Why do you ask if I can {0}?")),
    PhraseMatcher("can I ([^\\?]*)\\??", Seq("Perhaps you don't want to {0}.", "Do you want to be able to {0}?", "If you could {0}, would you?")),
    PhraseMatcher("you are (.*)", Seq("Why do you think I am {0}?", "Does it please you to think that I'm {0}?", "Perhaps you would like me to be {0}.", "Perhaps you're really talking about yourself?")),
    PhraseMatcher("you'?re (.*)", Seq("Why do you say I am {0}?", "Why do you think I am {0}?", "Are we talking about you, or me?")),
    PhraseMatcher("i don'?t (.*)", Seq("Don't you really {0}?", "Why don't you {0}?", "Do you want to {0}?")),
    PhraseMatcher("i feel (.*)", Seq("Good, tell me more about these feelings.", "Do you often feel {0}?", "When do you usually feel {0}?", "When you feel {0}, what do you do?")),
    PhraseMatcher("i have (.*)", Seq("Why do you tell me that you've {0}?", "Have you really {0}?", "Now that you have {0}, what will you do next?")),
    PhraseMatcher("i would (.*)", Seq("Could you explain why you would {0}?", "Why would you {0}?", "Who else knows that you would {0}?")),
    PhraseMatcher("is there (.*)", Seq("Do you think there is {0}?", "It's likely that there is {0}.", "Would you like there to be {0}?")),
    PhraseMatcher("my (.*)", Seq("I see, your {0}.", "Why do you say that your {0}?", "When your {0}, how do you feel?")),
    PhraseMatcher("you (.*)", Seq("We should be discussing you, not me.", "Why do you say that about me?", "Why do you care whether I {0}?")),
    PhraseMatcher("why (.*)", Seq("Why don't you tell me the reason why {0}?", "Why do you think {0}?")),
    PhraseMatcher("i want (.*)", Seq("What would it mean to you if you got {0}?", "Why do you want {0}?", "What would you do if you got {0}?", "If you got {0}, then what would you do?")),
    PhraseMatcher("(.*) mother(.*)", Seq("Tell me more about your mother.", "What was your relationship with your mother like?", "How do you feel about your mother?", "How does this relate to your feelings today?", "Good family relations are important.")),
    PhraseMatcher("(.*) father(.*)", Seq("Tell me more about your father.", "How did your father make you feel?", "How do you feel about your father?", "Does your relationship with your father relate to your feelings today?", "Do you have trouble showing affection with your family?")),
    PhraseMatcher("(.*) child(.*)", Seq("Did you have close friends as a child?", "What is your favorite childhood memory?", "Do you remember any dreams or nightmares from childhood?", "Did the other children sometimes tease you?", "How do you think your childhood experiences relate to your feelings today?")),
    PhraseMatcher("(.*)\\?", Seq("Why do you ask that?", "Please consider whether you can answer your own question.", "Perhaps the answer lies within yourself?", "Why don't you tell me?")),
    PhraseMatcher("quit", Seq("Thank you for talking with me.", "Good-bye.", "Thank you, that will be $150.  Have a good day!")),
    PhraseMatcher("(.*)", Seq("Please tell me more.", "Let's change focus a bit... Tell me about your family.", "Can you elaborate on that?", "Why do you say that, {0}?", "I see.", "Very interesting.", "{0}.", "I see.  And what does that tell you?", "How does that make you feel?", "How do you feel when you say that?")),
  )


  def prepareResponse(humansText: String): String =
    phrases.map(matcher => matcher.respond(humansText.toLowerCase)).filter(_.nonEmpty).head


  def main(args: Array[String]): Unit = {
    println("Hi! I am ELIZA - phatic dialogue bot.")
    while (true) {
      val text = Source.stdin.bufferedReader().readLine()
      println(prepareResponse(text))
    }
  }

}
