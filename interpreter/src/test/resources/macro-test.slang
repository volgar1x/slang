(defmacro if [bool block else]
  '(cond
     (#bool #block)
     ('else #else)))

(defmacro unless [bool block else]
  '(if (not #bool)
       #block
       #else))

(def is-enough-potatos [nr-of-potatos]
  (unless (<= nr-of-potatos 30) 'enough 'not-enough))

(assert = 'enough (is-enough-potatos 69))
(assert = 'not-enough (is-enough-potatos 0))

(defmacro print-vars-rec [xs acc]
  (cond
    ((nil? xs) (.reverse acc))
    ('else
      (print-vars-rec (cdr xs)
          (cons
		    (let [var (car xs) var-name (.toString var)]
		      '(println #var-name "=" #var))
		    acc)))))

(defmacro print-vars [& vars] (print-vars-rec vars ()))

(let [foo "bar" buzz 42]
  (print-vars foo buzz))

(defmacro thread-form [receiver form]
  (cons (car form)
    (cons receiver (cdr form))))

(defmacro thread-forms-rec [xs acc]
  (cond
    ((nil? xs) acc)
    (:else
      (thread-forms-rec (cdr xs)
        (thread-form acc (car xs))))))

(defmacro -> [x & forms]
  (thread-forms-rec forms x))

(assert = '("4" "8")
  (-> '(1 2 3 4)
    (.stream)
    (.filter (fn [x] (= 0 (% x 2))))
    (.map (fn [x] (* x 2)))
    (.map (fn [x] (.toString x)))
    (.collect (slang.stream.Collectors/list))))

