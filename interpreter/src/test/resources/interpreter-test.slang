(println "Hello, World!")

(let [name "World"]
  (let [name "Monde"]
    (println "Hello, " name "!"))
  (println "Hello, " name "!"))

(let [name (readln "whats your name?")]
  (println "Hi " name " !"))

(def plus-one [num]
  (+ num 1))

(assert = "nil" (inspect ()))
(assert = "nil" (inspect '()))
(assert = "nil" (inspect nil))

(assert = "atom" (inspect 'atom))
(assert = 2 (+ 1 1))
(assert = 0 (- 1 1))
(assert = 0 (* 1 0))
(assert = 0.0 (/ 0 1))
(assert = 0 (% 1 1))
(assert = 1.0 (sqrt 1.0))
;(assert = 0 (cos 1.57079))
;(assert = 0 (sin 3.14159))
;(assert = 0 (tan 3.14159))

(assert = 1 (plus-one 0))
(assert = 0 (- (+ 0 1) 1))
(assert = 1 (plus-one (- 1 1)))

(def fibo-rec [n acc next]
    (cond ((= n 0) acc)
        ('else (fibo-rec (- n 1) next (+ acc next)))))
(def fibo [n] (fibo-rec n 0 1))

(assert = 55 (fibo 10))

(assert = 4 (.length "abcd"))                     ;instance method call
(assert = "55" (java.lang.Integer/toString 55))   ;static method call
(assert = -1.0 (java.lang.Math/cos (java.lang.Math#PI)))
(assert = 444 (.add (java.math.BigInteger. "123") (java.math.BigInteger. "321")))
(assert = "java.lang.String" (.getName (.getClass "string")))

(def with-varargs [hd & tl] (car (cdr tl)))
(assert = 3 (with-varargs 1 2 3))

(def call-lambda-function [fun] (* 2 (fun)))

(assert = 42 (call-lambda-function (fn [] (+ 12 9))))
