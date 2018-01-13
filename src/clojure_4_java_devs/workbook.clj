;; gorilla-repl.fileformat = 1

;; **
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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;user/caesar-cipher</span>","value":"#'user/caesar-cipher"}
;; <=

;; **
;;; ## Lots of stuff going on here!
;;; 
;;; * prefix notation/s-expressions
;;; * function definitions
;;; * function arguments
;;; * function comments
;;; * local bindings (via let)
;;; * function invocation
;;; * first class functions
;;; * core api usage
;;; 
;;; _Let's look at each of these in turn..._
;; **

;; **
;;; ### Prefix Notation
;;; 
;;; * Function comes first, then arg list
;;; * ```(println "Hello World")``` versus ```System.out.println("Hello world");```
;;; * Almost everything is an s-expression
;; **

;; @@
(println "Hello World")
(+ 3 4)
;; @@
;; ->
;;; Hello World
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-long'>7</span>","value":"7"}
;; <=

;; **
;;; ### Function Definition
;; **

;; @@
(user/caesar-cipher)
;; @@
