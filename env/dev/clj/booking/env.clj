(ns booking.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [booking.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[booking started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[booking has shut down successfully]=-"))
   :middleware wrap-dev})
