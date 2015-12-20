(defmacro if [bool block else]
  '(case
     (#bool #block)
     (:t #else)))

(defmacro unless [bool block else]
  '(if (not #bool)
       #block
       #else))

(def is-enough-potatos [nr-of-potatos]
  (unless (< nr-of-potatos 30) :enough :not-enough))

(assert = :enough (is-enough-potatos 69))
(assert = :not-enough (is-enough-potatos 0))