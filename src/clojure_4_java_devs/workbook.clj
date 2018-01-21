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
;;; ## Our first look at Clojure
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
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;khoor zruog&quot;</span>","value":"\"khoor zruog\""}
;; <=

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
;;; 1. core api usage (```map/char/cycle/take/drop/zipmap/assoc/apply/str```)
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
;; ->
;;; Hello World
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-long'>-1</span>","value":"-1"}
;; <=

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
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;user/caesar-cipher</span>","value":"#'user/caesar-cipher"}
;; <=

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
;; ->
;;; (1 2 3 4 5)
;;; (1 2 3 4 5)
;;; nil
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

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
;; ->
;;; -------------------------
;;; user/caesar-cipher
;;; ([words offset])
;;;   Assumes offset &gt;=0, words entirely lowercase English characters or spaces
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

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
;; =>
;;; {"type":"html","content":"<span class='clj-long'>3</span>","value":"3"}
;; <=

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
;;; So what do our other functions do?
;;; 
;; **

;; @@
(drop 1 [1 2 3 4])
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"},{"type":"html","content":"<span class='clj-long'>4</span>","value":"4"}],"value":"(2 3 4)"}
;; <=

;; @@
(take 1 [1 2 3 4])
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>1</span>","value":"1"}],"value":"(1)"}
;; <=

;; @@
(take 3 (cycle ["Ho! "]))
(apply str (take 3 (cycle ["Ho! "])))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Ho! Ho! Ho! &quot;</span>","value":"\"Ho! Ho! Ho! \""}
;; <=

;; **
;;; * Note: ```cycle``` is "lazy", so don't call in isolation (or if you're in a hurry :-))
;; **

;; @@
(->> (cycle (map char "abcdefghijklmnopqrstuvwxyz")) (take 100) (drop 5))
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"},{"type":"html","content":"<span class='clj-char'>\\w</span>","value":"\\w"},{"type":"html","content":"<span class='clj-char'>\\x</span>","value":"\\x"},{"type":"html","content":"<span class='clj-char'>\\y</span>","value":"\\y"},{"type":"html","content":"<span class='clj-char'>\\z</span>","value":"\\z"},{"type":"html","content":"<span class='clj-char'>\\a</span>","value":"\\a"},{"type":"html","content":"<span class='clj-char'>\\b</span>","value":"\\b"},{"type":"html","content":"<span class='clj-char'>\\c</span>","value":"\\c"},{"type":"html","content":"<span class='clj-char'>\\d</span>","value":"\\d"},{"type":"html","content":"<span class='clj-char'>\\e</span>","value":"\\e"},{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"},{"type":"html","content":"<span class='clj-char'>\\w</span>","value":"\\w"},{"type":"html","content":"<span class='clj-char'>\\x</span>","value":"\\x"},{"type":"html","content":"<span class='clj-char'>\\y</span>","value":"\\y"},{"type":"html","content":"<span class='clj-char'>\\z</span>","value":"\\z"},{"type":"html","content":"<span class='clj-char'>\\a</span>","value":"\\a"},{"type":"html","content":"<span class='clj-char'>\\b</span>","value":"\\b"},{"type":"html","content":"<span class='clj-char'>\\c</span>","value":"\\c"},{"type":"html","content":"<span class='clj-char'>\\d</span>","value":"\\d"},{"type":"html","content":"<span class='clj-char'>\\e</span>","value":"\\e"},{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"},{"type":"html","content":"<span class='clj-char'>\\w</span>","value":"\\w"},{"type":"html","content":"<span class='clj-char'>\\x</span>","value":"\\x"},{"type":"html","content":"<span class='clj-char'>\\y</span>","value":"\\y"},{"type":"html","content":"<span class='clj-char'>\\z</span>","value":"\\z"},{"type":"html","content":"<span class='clj-char'>\\a</span>","value":"\\a"},{"type":"html","content":"<span class='clj-char'>\\b</span>","value":"\\b"},{"type":"html","content":"<span class='clj-char'>\\c</span>","value":"\\c"},{"type":"html","content":"<span class='clj-char'>\\d</span>","value":"\\d"},{"type":"html","content":"<span class='clj-char'>\\e</span>","value":"\\e"},{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"}],"value":"(\\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v \\w \\x \\y \\z \\a \\b \\c \\d \\e \\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v \\w \\x \\y \\z \\a \\b \\c \\d \\e \\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v \\w \\x \\y \\z \\a \\b \\c \\d \\e \\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v)"}
;; <=

;; @@
(drop 5 (take 100 (cycle (map char "abcdefghijklmnopqrstuvwxyz"))))
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-lazy-seq'>(</span>","close":"<span class='clj-lazy-seq'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"},{"type":"html","content":"<span class='clj-char'>\\w</span>","value":"\\w"},{"type":"html","content":"<span class='clj-char'>\\x</span>","value":"\\x"},{"type":"html","content":"<span class='clj-char'>\\y</span>","value":"\\y"},{"type":"html","content":"<span class='clj-char'>\\z</span>","value":"\\z"},{"type":"html","content":"<span class='clj-char'>\\a</span>","value":"\\a"},{"type":"html","content":"<span class='clj-char'>\\b</span>","value":"\\b"},{"type":"html","content":"<span class='clj-char'>\\c</span>","value":"\\c"},{"type":"html","content":"<span class='clj-char'>\\d</span>","value":"\\d"},{"type":"html","content":"<span class='clj-char'>\\e</span>","value":"\\e"},{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"},{"type":"html","content":"<span class='clj-char'>\\w</span>","value":"\\w"},{"type":"html","content":"<span class='clj-char'>\\x</span>","value":"\\x"},{"type":"html","content":"<span class='clj-char'>\\y</span>","value":"\\y"},{"type":"html","content":"<span class='clj-char'>\\z</span>","value":"\\z"},{"type":"html","content":"<span class='clj-char'>\\a</span>","value":"\\a"},{"type":"html","content":"<span class='clj-char'>\\b</span>","value":"\\b"},{"type":"html","content":"<span class='clj-char'>\\c</span>","value":"\\c"},{"type":"html","content":"<span class='clj-char'>\\d</span>","value":"\\d"},{"type":"html","content":"<span class='clj-char'>\\e</span>","value":"\\e"},{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"},{"type":"html","content":"<span class='clj-char'>\\w</span>","value":"\\w"},{"type":"html","content":"<span class='clj-char'>\\x</span>","value":"\\x"},{"type":"html","content":"<span class='clj-char'>\\y</span>","value":"\\y"},{"type":"html","content":"<span class='clj-char'>\\z</span>","value":"\\z"},{"type":"html","content":"<span class='clj-char'>\\a</span>","value":"\\a"},{"type":"html","content":"<span class='clj-char'>\\b</span>","value":"\\b"},{"type":"html","content":"<span class='clj-char'>\\c</span>","value":"\\c"},{"type":"html","content":"<span class='clj-char'>\\d</span>","value":"\\d"},{"type":"html","content":"<span class='clj-char'>\\e</span>","value":"\\e"},{"type":"html","content":"<span class='clj-char'>\\f</span>","value":"\\f"},{"type":"html","content":"<span class='clj-char'>\\g</span>","value":"\\g"},{"type":"html","content":"<span class='clj-char'>\\h</span>","value":"\\h"},{"type":"html","content":"<span class='clj-char'>\\i</span>","value":"\\i"},{"type":"html","content":"<span class='clj-char'>\\j</span>","value":"\\j"},{"type":"html","content":"<span class='clj-char'>\\k</span>","value":"\\k"},{"type":"html","content":"<span class='clj-char'>\\l</span>","value":"\\l"},{"type":"html","content":"<span class='clj-char'>\\m</span>","value":"\\m"},{"type":"html","content":"<span class='clj-char'>\\n</span>","value":"\\n"},{"type":"html","content":"<span class='clj-char'>\\o</span>","value":"\\o"},{"type":"html","content":"<span class='clj-char'>\\p</span>","value":"\\p"},{"type":"html","content":"<span class='clj-char'>\\q</span>","value":"\\q"},{"type":"html","content":"<span class='clj-char'>\\r</span>","value":"\\r"},{"type":"html","content":"<span class='clj-char'>\\s</span>","value":"\\s"},{"type":"html","content":"<span class='clj-char'>\\t</span>","value":"\\t"},{"type":"html","content":"<span class='clj-char'>\\u</span>","value":"\\u"},{"type":"html","content":"<span class='clj-char'>\\v</span>","value":"\\v"}],"value":"(\\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v \\w \\x \\y \\z \\a \\b \\c \\d \\e \\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v \\w \\x \\y \\z \\a \\b \\c \\d \\e \\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v \\w \\x \\y \\z \\a \\b \\c \\d \\e \\f \\g \\h \\i \\j \\k \\l \\m \\n \\o \\p \\q \\r \\s \\t \\u \\v)"}
;; <=

;; **
;;; Question: can you see what the ```->>``` macro is doing here?
;; **

;; @@
(zipmap [1 2 3 4] [\a \b \c])
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>1</span>","value":"1"},{"type":"html","content":"<span class='clj-char'>\\a</span>","value":"\\a"}],"value":"[1 \\a]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>2</span>","value":"2"},{"type":"html","content":"<span class='clj-char'>\\b</span>","value":"\\b"}],"value":"[2 \\b]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"},{"type":"html","content":"<span class='clj-char'>\\c</span>","value":"\\c"}],"value":"[3 \\c]"}],"value":"{1 \\a, 2 \\b, 3 \\c}"}
;; <=

;; @@
(assoc {:key1 "value"} :key2 "another value")
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:key1</span>","value":":key1"},{"type":"html","content":"<span class='clj-string'>&quot;value&quot;</span>","value":"\"value\""}],"value":"[:key1 \"value\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:key2</span>","value":":key2"},{"type":"html","content":"<span class='clj-string'>&quot;another value&quot;</span>","value":"\"another value\""}],"value":"[:key2 \"another value\"]"}],"value":"{:key1 \"value\", :key2 \"another value\"}"}
;; <=

;; **
;;; ## What Else?
;; **

;; @@

;; @@

;; **
;;; ### Java Interop
;; **

;; **
;;; ### The Read Eval Print Loop (REPL)
;; **

;; **
;;; ### Functional Thinking
;; **

;; **
;;; 
;; **
