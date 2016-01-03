(defmacro if [cond block else]
  '(cond
     (#cond #block)
     ('else #else)))

(defmacro unless [cond block else]
  '(cond
     (#cond #block)
     ('else #else)))

(def fibo-rec [n acc next]
    (if (= n 0)
        acc
        (fibo-rec (- n 1) next (+ acc next))))
(def fibo [n] (fibo-rec n 0 1))

(def loop [n last-result ops]
  (if (= n 0)
      last-result
      (loop (- n 1) (eval ops) ops)))

(loop 1000 0 '(fibo 100))
