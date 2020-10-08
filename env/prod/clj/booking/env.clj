(ns booking.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[booking started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[booking has shut down successfully]=-"))
   :middleware identity})
