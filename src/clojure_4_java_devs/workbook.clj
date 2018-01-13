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
(defn caesar-cipher [words offset]
  "Assumes offset >=0, words entirely lowercase English characters or spaces"
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
;;; 1. function definitions
;;; 1. function arguments
;;; 1. function comments
;;; 1. local bindings (via let)
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
;;; 
;; **

;; @@
(println "Hello World")
(+ 3 4)
(+ 1 2 3 4) ;; function name only needed once
(/ 4 (- 1 (+ 2 3))) ;; this takes a bit of getting used to
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
;;; ### 2. Function Definition
;;; 
;;; * ```def``` is a function that creates a global variable with a name and namespace
;;; * ```fn``` is a function that creates an (usually) anonymous function
;;; * ```defn``` is a macro that combines ```def``` and ```fn``` to define a named function
;;; * Note: no explicit typing of function return type
;; **

;; @@
(defn hello [] "Hello World") ;; Creates 0-arg function in user (default) namespace
(hello) ;; calls function 
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Hello World&quot;</span>","value":"\"Hello World\""}
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
;;; * Variadic arguments can be grouped
;;; * Lots of support for destructing arguments
;; **

;; **
;;; ```
;;; (defn caesar-cipher [words offset] ...)
;;; ```
;; **

;; **
;;; ### 4. Function Comments
;; **

;; **
;;; ### 5. Local Bindings
;; **

;; **
;;; ### 6. Function Invocation
;; **

;; **
;;; ### 7. First Class Functions
;; **

;; **
;;; ### 8. Core API Usage
;; **

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
