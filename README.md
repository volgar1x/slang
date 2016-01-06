slang [![Build Status](https://travis-ci.org/Blackrush/slang.svg)](https://travis-ci.org/Blackrush/slang) [![Coverage Status](https://coveralls.io/repos/Blackrush/slang/badge.svg?branch=master&service=github)](https://coveralls.io/github/Blackrush/slang?branch=master)
=====

### How to build

`./gradlew build`


### Getting started

Arithmetics

```
(+ 1 (* 2 (% 11 4)))
```

Call a function

```
(println "Hello, World!")
```

Declaring variables

```
(let [name (readln "What is your name? ")]
   (println "Hello, " name "!"))
```

Conditionals

```
(let [passwd (readln "Enter password: ")]
   (cond
      ((= passwd "s3cr3t")
         (println "Hello admin!"))
      ((= passwd "azerty")
         (println "Frenchies are not allowed here!"))
      (:else
         (println "Access denied."))))
```

Declaring functions

```
(def hello [name]
   (println "Hello, " name "!"))

(hello "World")
(hello (readln "What's your name? "))
```

Operations on lists

```
(assert = '(1 2 3)
   (cons 1 '(2 3))

(assert = 1 (car '(1 2 3))

(assert = '(2 3)
  (cdr '(1 2 3))
```

Creating a list

```
(def print-list [xs]
   (cond
      ((nil? xs) nil)
      (:else
         (println (car xs))
         (print-list (cdr xs)))))

(print-list '("foo" "bar" "buzz"))
```

Call Java APIs

```
;; static fields
(println (java.lang.Math#PI))

;; new instance & method call
(let [builder (java.lang.StringBuilder.)]
  (.append builder "Hello, ")
  (.append builder "World!")
  (println (.toString builder)))

;; static method calls
(println (java.lang.Math/abs -1))
(println (java.lang.Math/cos 3.14159))
(println (java.lang.Math/log (* 10 (java.lang.Math#E))))
```

Macros

```
(defmacro if [condition block else]
   '(cond
       (#condition #block)
       (:else #else)))

(if (= "s3cr3t" (readln "password: "))
    (println "Access granted.")
    (println "Access denied."))
```

Macros & Java APIs

```
(defmacro thread-form [receiver form]
   (cons (car form)
      (cons receiver (cdr form))))

(defmacro thread-forms-rec [xs acc]
   (cond
      ((nil? xs) acc)
      (:else
         (thread-forms-rec (cdr xs)
         (thread-form acc (car xs))))))

;; https://clojuredocs.org/clojure.core/-%3E
(defmacro -> [x & forms]
   (thread-forms-rec forms x))

(assert = '("4" "8")
   (-> '(1 2 3 4)
      (.stream)
      (.filter (fn [x] (= 0 (% x 2))))
      (.map (fn [x] (* x 2)))
      (.map (fn [x] (.toString x)))
      (.collect (slang.stream.Collectors/list))))
```
