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
(assert = "nil" (inspect :nil))
(assert = "nil" (inspect :NIL))
(assert = "nil" (inspect nil))

(assert = ":atom" (inspect :atom))
(assert = 2 (+ 1 1))
(assert = 0 (- 1 1))
(assert = 0 (* 1 0))
(assert = 0 (** 0 1))
(assert = 0 (/ 0 1))
(assert = 0 (% 1 1))
(assert = 1.0 (sqrt 1.0))
;(assert = 0 (cos 1.57079))
;(assert = 0 (sin 3.14159))
;(assert = 0 (tan 3.14159))

(assert = 1 (plus-one 0))
(assert = 0 (- (+ 0 1) 1))
(assert = 1 (plus-one (- 1 1)))

(assert = 1.41421356237 (round (sqrt 2.0) 11))

(def fibo-rec [n acc next]
    (case ((= n 0) acc)
        (:t (fibo-rec (- n 1) next (+ acc next)))))
(def fibo [n] (fibo-rec n 0 1))

(assert = 55 (fibo 10))