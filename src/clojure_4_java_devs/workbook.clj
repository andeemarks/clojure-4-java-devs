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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;world&quot;</span>","value":"\"world\""}
;; <=

;; **
;;; * Clojure supports "first class functions"
;;; * A first class function can be passed as a parameter
;;; * A first class function can be returned from a function
;;; * ```map``` takes a function as the first argument and applies it to each element of the second argument (a collection), returning a collection
;;; * ```apply``` takes a function as the first argument and applies it to each element of the second argument (a collection), returning a single result
;; **

;; **
;;; ### 8. Core API Usage
;; **

;; @@

;; @@

;; **
;;; ## What Else?
;; **

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
