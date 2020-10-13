(ns booking.db.core
  (:require
   [datomic.api :as d]
   [io.rkn.conformity :as c]
   [mount.core :refer [defstate]]
   [booking.config :refer [env]]))

(defstate conn
  :start (do (-> env :database-url d/create-database) (-> env :database-url d/connect))
  :stop (-> conn .release))

(defn setup-app-db [fname]
  (let [norms-map (c/read-resource fname)]
    (c/ensure-conforms conn norms-map)))

(defn install-schema
  "This function expected to be called at system start up.

  Datomic schema migrations or db preinstalled data can be put into 'migrations/schema.edn'
  Every txes will be executed exactly once no matter how many times system restart."
  [conn]
  (let [norms-map (c/read-resource "migrations/schema.edn")]
    (c/ensure-conforms conn norms-map (keys norms-map))))

(defn show-schema
  "Show currently installed schema"
  [conn]
  (let [system-ns #{"db" "db.type" "db.install" "db.part"
                    "db.lang" "fressian" "db.unique" "db.excise"
                    "db.cardinality" "db.fn" "db.sys" "db.bootstrap"
                    "db.alter"}]
    (d/q '[:find ?ident
           :in $ ?system-ns
           :where
           [?e :db/ident ?ident]
           [(namespace ?ident) ?ns]
           [((comp not contains?) ?system-ns ?ns)]]
         (d/db conn) system-ns)))

(defn show-transaction
  "Show all the transaction data
   e.g.
    (-> conn show-transaction count)
    => the number of transaction"
  [conn]
  (seq (d/tx-range (d/log conn) nil nil)))

(defn time-range->inventory-eids
  [db begin end]
  (d/q '[:find [?e ...]
         :in $ ?date-begin ?date-end ?less
         :where
         [?e :calendar/date ?d]
         [(missing? $ ?e :calendar/booking)]
         [(compare ?date-begin ?d) ?less]
         [(compare ?d ?date-end) ?less]]
       db begin end -1))

(defn time-range->booking-eids
  [db begin end]
  (d/q '[:find [?e ...]
         :in $ ?date-begin ?date-end ?less
         :where
         [?e :calendar/date ?d]
         [?e :calendar/booking _]
         [(compare ?date-begin ?d) ?less]
         [(compare ?d ?date-end) ?less]]
       db begin end -1))

(defn eid->calendar-entity [db eid]
  (d/pull db '[:calendar/date
               {:calendar/booking [*]}
               {:calendar/product [:product/id]}] eid))

;; testing code
(def d-begin #inst "2020-01-04")
(def d-end #inst "2020-01-07")

(def show-time-range-inventory
  (map
   #(eid->calendar-entity (d/db conn) %)
   (time-range->inventory-eids (d/db conn) d-begin d-end)))

(def show-time-range-booking
  (map
   #(eid->calendar-entity (d/db conn) %)
   (time-range->booking-eids (d/db conn) d-begin d-end)))
