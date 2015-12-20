(println "Hello, World!")

(let [name "World"]
  (let [name "Monde"]
    (println "Hello, " name "!"))
  (println "Hello, " name "!"))

(let [name (readln "whats your name?")]
  (println "Hi " name " !"))

(def plus-one [num]
  (+ num 1))

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