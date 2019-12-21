(ns deputados.io
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clj-time.format :as tf]))


(defn read-resource
  [filename]
  (when-let [res (io/resource filename)]
    (-> res
        slurp
        s/split-lines)))


(defn parse-line
  [line]
  (s/split line #";"))


(defn parse-string
  [line]
  (map read-string line))


(defn create-deputy
  [header
   data]
  (zipmap header data))

(defn fix-headers
  [headers]
  (let [fixed (parse-string (parse-line headers))]
    (map keyword (concat ["uri"] (rest fixed)))))

(defn headers
  [data]
  (-> data
      first
      fix-headers))


(defn create-deputy
  [header]
  (fn [deputy]
    (zipmap header deputy)))

(defn parse-date
  [deputy]
  (-> deputy
      (update :dataNascimento tf/parse)
      (update :dataFalecimento tf/parse)))

(defn- to-int
  [num]
  (Integer/parseInt num))

(defn parse-ints
  [deputy]
  (-> deputy
      (update :idLegislaturaInicial to-int)
      (update :idLegislaturaFinal to-int)))

(defn parse
  [data]
  (let [hdrs (headers data)
        deps (rest data)]
    (->> deps
         (map parse-line)
         (map parse-string)
         (map (create-deputy hdrs))
         (map parse-date)
         (map parse-ints))))
