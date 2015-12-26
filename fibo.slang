(defmacro if [cond block else]
  '(case
     (#cond #block)
     (:t #else)))

(defmacro unless [cond block else]
  '(case
     (#cond #block)
     (:t #else)))

(def fibo-rec [n acc next]
    (if (= n 0)
        acc
        (fibo-rec (- n 1) next (+ acc next))))
(def fibo [n] (fibo-rec n 0 1))

(assert = 55 (fibo 10))
(assert = 354224848179261915075 (fibo 100))
