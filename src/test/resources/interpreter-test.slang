(println "Hello, World!")

(let [name "World"]
  (let [name "Monde"]
    (println "Hello, " name "!"))
  (println "Hello, " name "!"))

(let [name (readln "whats your name?")]
  (println "Hi " name " !"))