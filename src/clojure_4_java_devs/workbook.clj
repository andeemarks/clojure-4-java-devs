;; gorilla-repl.fileformat = 1

;; **
;;; ![Logo](http://i.imgur.com/1GjPKvB.png)
;;; 
;;; # An Intro to Clojure (for Java Developers)
;;; 
;;;  By Andy Marks
;;;  > Email: _Andrew.X.Marks@nab.com.au_
;;;  
;;;  > Room: _12.622_
;;;  
;;;  > Twitter: _@andee_marks_
;; **

;; **
;;; ### Our first look at Clojure
;;; 
;;; Firtly, here's a basic cipher algorithm in Java...
;;; 
;;; ```
;;; private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
;;; 
;;; public String caesarCipher(String words, int offset) {
;;; 	plainText = plainText.toLowerCase();
;;;     String cipherText = "";
;;;     for (int i = 0; i < plainText.length(); i++) {
;;;         int charPosition = ALPHABET.indexOf(plainText.charAt(i));
;;;         int keyVal = (offset + charPosition) % 26;
;;;         char replaceVal = ALPHABET.charAt(keyVal);
;;;         cipherText += replaceVal;
;;;     }
;;;     return cipherText;
;;; }
;;;     ```
;;; And here it is in Clojure...
;; **

;; @@
(defn caesar-cipher 
  "Assumes offset >=0, words entirely lowercase English characters or spaces"
  [words offset]
  (let [alphabet-chars (map char "abcdefghijklmnopqrstuvwxyz")
        alphabet-shifted (->> (cycle alphabet-chars) (take 100) (drop offset))
        shifted-map (-> (zipmap alphabet-chars alphabet-shifted)
                        (assoc \space \space))]
    (apply str (map shifted-map (map char words)))))

(caesar-cipher "hello world" 3)
;; @@

;; **
;;; ## Lots of stuff going on here!
;;; 
;;; 1. prefix notation/s-expressions
;;; 1. function definitions ("```defn```")
;;; 1. function arguments ("```[...]```")
;;; 1. function comments 
;;; 1. local bindings ("```let```")
;;; 1. function invocation
;;; 1. first class functions
;;; 1. core api usage
;;; 
;;; _Let's look at each of these in turn..._
;; **

;; **
;;; ### 1. Prefix Notation
;;; 
;;; * Function name comes first, then arg list
;;; * Everything comes in parenthesis
;;; * ```(println "Hello World")``` versus ```System.out.println("Hello world");```
;;; * _Almost_ everything is an s-expression
;;; * Further examples:
;; **

;; @@
(println "Hello World")
(+ 3 4)
(+ 1 2 3 4) ;; "+" function name only needed once
(/ 4 (- 1 (+ 2 3))) ;; nested function calls - this takes a bit of getting used to
;; @@

;; **
;;; ![XKCD](https://imgs.xkcd.com/comics/lisp_cycles.png)
;; **

;; **
;;; ### 2. Function Definitions
;;; 
;;; * Options:
;;;  * ```def``` is a function that creates a global variable with a name and namespace
;;;  * ```fn``` is a function that creates an (usually) anonymous function
;;;  * ```defn``` is a macro that composes ```def``` and ```fn``` to define a named function
;;; * Functions are public by default
;;; * Note: no explicit typing of function return type or parameters
;; **

;; @@
(defn hello [] "Hello World") ;; Creates 0-arg function in user (default) namespace
(hello) ;; calls function 

(defn caesar-cipher [words offset]) ;; same function definition - empty implementation
;; @@

;; **
;;; The equivalent in Java is
;;; ~~~~
;;; public String hello() {
;;; 	return "Hello World"
;;; };
;;; ~~~~
;;; 
;; **

;; **
;;; ### 3. Function Arguments
;;; 
;;; * All arguments names in square brackets in function definition
;;; * Optional arguments can be grouped
;;; * Extra: Support for destructing arguments (e.g., pulling elements out of maps by key)
;; **

;; **
;;; ```
;;; (defn caesar-cipher [words offset] ...)
;;; ```
;;; 
;;; In Java, this would be:
;;; ```
;;; public String caesarCipher (String words, int offset) {...};
;;; ```
;; **

;; @@
(defn print-args [& args] 
  (println args)) ;; variadic function definition

(print-args 1 2 3 4 5)
(print-args "1" "2" "3" 4 5)
(print-args)
;; @@

;; **
;;; ### 4. Function Comments
;;; 
;;; * Quoted string between function arguments and implementation
;;; * Equivalent to Javadoc
;;; * Printable via core ```doc``` function
;; **

;; @@
(defn caesar-cipher 
  "Assumes offset >=0, words entirely lowercase English characters or spaces"		[words offset])

(clojure.repl/doc caesar-cipher) ;; note: we're calling a function in another namespace

;; @@

;; **
;;; ### 5. Local Bindings
;;; 
;;; * ```let``` is a function that takes a collection of name/value pairs
;;; * Scope is local to the ```(let [name1 value1 name2 value2...] ...)``` block
;;; * Values cannot be re-bound (i.e., immutable)
;;; 
;;; Our ```caesar-cipher``` binds values to three variables within the ```let``` block...
;; **

;; @@
(defn caesar-cipher 
  "Assumes offset >=0, words entirely lowercase English characters or spaces"
  [words offset]
  (let [alphabet-chars (map char "abcdefghijklmnopqrstuvwxyz")
        alphabet-shifted (->> (cycle alphabet-chars) (take 100) (drop offset))
        shifted-map (-> (zipmap alphabet-chars alphabet-shifted)
                        (assoc \space \space))]
    (apply str (map shifted-map (map char words)))))
;; @@

;; **
;;; In Java, this is the equivalent of...
;; **

;; @@
public String caesarCipher (String words, int offset) {
  char[] alphabetChars = ...;
  char[] alphabetShifted = ...;
  ...
};
                                                        
                                                      
;; @@

;; **
;;; ### 6. Function Invocation
;;; 
;;; Firstly, let's see which functions are being explicitly called in our solution:
;;; 
;;; * ```let```
;;; * ```map``` (3 times)
;;; * ```cycle```
;;; * ```take```
;;; * ```drop```
;;; * ```zipmap```
;;; * ```assoc```
;;; * ```apply```
;;; 
;;; And which ones are being called behind-the-scenes:
;;; 
;;; * ```char``` (2 times)
;;; * ```str```
;;; 
;;; Almost everything following a left parenthesis is a function call, but there are some things which are actually macro invocations:
;;; 
;;; * ```defn``` (we've seen this before)
;;; * ```->``` 
;;; * ```->>```
;;; 
;;; Between the functions, the macros and the ```let``` special form, that's the entire solution!
;; **

;; **
;;; ### 7. First Class Functions
;;; 
;;; In our solution, we said ```char``` and ```str``` were called "behind-the-scenes"!?!
;;; 
;;; 
;; **

;; @@
(map char "hello")
(apply str (map char "world"))

(max [1 2 3])
(apply max [1 2 3])
;; @@

;; **
;;; * Clojure supports "first class functions" (aka "higher order functions")
;;; * A first class function can be passed as a parameter
;;; * A first class function can be returned from a function
;;; * ```map``` applies its first argument (a function) to each element of the second argument (a collection), returning a collection
;;; * ```apply``` applies its first argument (a function) to each element of the second argument (a collection), returning a single result
;;; * In Java < 8, you could mimic function references via objects and interfaces
;;; * In Java >= 8, you get first class functions through method references and lambdas
;; **

;; **
;;; ### 8. Core API Usage
;;; 
;;; _So what do our other functions do?_
;;; 
;; **

;; @@
(drop 1 [1 2 3 4])
;; @@

;; @@
(take 1 [1 2 3 4])
;; @@

;; @@
(take 3 (cycle ["Ho! "]))
(apply str (take 3 (cycle ["Ho! "])))
;; @@

;; **
;;; * Note: ```cycle``` is "lazy", so don't call in isolation (or if you're in a hurry :-))
;; **

;; @@
(->> (cycle (map char "abcdefghijklmnopqrstuvwxyz")) (take 100) (drop 5))
;; @@

;; @@
(drop 5 (take 100 (cycle (map char "abcdefghijklmnopqrstuvwxyz"))))
;; @@

;; **
;;; Question: _can you see what the ```->>``` macro is doing here?_
;; **

;; @@
(zipmap [1 2 3 4] [\a \b \c])
;; @@

;; @@
(assoc {:key1 "value"} :key2 "another value")
;; @@

;; **
;;; ## What Else?
;; **

;; @@

;; @@

;; **
;;; ### Java Interop
;;; 
;;; * Clojure -> Java largely provided by the ```.``` special form
;; **

;; @@
(.toUpperCase "fred")

(System/getProperty "java.vm.version")

(.getName String)

Math/PI

(doto (new java.util.HashMap) (.put "a" 1) (.put "b" 2))

(bean java.awt.Color/black)
;; @@

;; **
;;; * Java -> Clojure via ```clojure.java.api.Clojure``` and ```clojure.lang.IFn``` classes
;; **

;; **
;;; ```
;;; // (+ 1 2)
;;; IFn plus = Clojure.var("clojure.core", "+");
;;; plus.invoke(1, 2);
;;; 
;;; // (map inc [1 2 3])
;;; IFn map = Clojure.var("clojure.core", "map");
;;; IFn inc = Clojure.var("clojure.core", "inc");
;;; map.invoke(inc, Clojure.read("[1 2 3]"));
;;; ```
;; **

;; **
;;; ### The Read Eval Print Loop (REPL)
;;; 
;;; * Very common part of modern, dynamic languages
;;; * Fast feedback loop for experimenting with code snippets
;;; * Super powerful when executable from IDE
;;; * Now part of Java 9
;; **

;; **
;;; ### Functional Thinking
;;; 
;;; * You'll will quickly go "parenthesis blind"
;;; * You will miss your explicit loops and conditionals... briefly
;;; * You will think about the data and how to transform it rather than how to model it
;;; * Syntax very simple, mastery comes from knowing how to compose functions
;; **

;; **
;;; 
;; **
