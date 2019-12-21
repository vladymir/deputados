(ns deputados.stat
  (:require [clj-time.core :as tc]))

(defn count-by
  [deputies
   attr
   value]
  (count (filter #(= value (% attr)) deputies)))

(defn count-sex
  [deputies
   sex]
  (count-by deputies :siglaSexo sex))

(defn count-men
  [deputies]
  (count-sex deputies "M"))

(defn count-women
  [deputies]
  (count-sex deputies "F"))

(defn years-between
  [born
   death]
  (tc/in-years (tc/interval born death)))

(defn valid-age?
  [deputy]
  nil)

(defn calculate-age
  [deputy]
  (when-let [born (:dataNascimento deputy)]
    (if-let [death (:dataFalecimento deputy)]
      (do
        (if (= 1 (compare born death))
          (println deputy))
        (years-between born death))
      (years-between born (tc/now)))))
