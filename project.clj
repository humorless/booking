(defproject booking "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[buddy/buddy-auth "2.2.0"]
                 [buddy/buddy-core "1.6.0"]
                 [buddy/buddy-hashers "1.4.0"]
                 [buddy/buddy-sign "3.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [cheshire "5.10.0"]
                 [clojure.java-time "0.3.2"]
                 [com.datomic/datomic-free "0.9.5697" :exclusions [org.slf4j/log4j-over-slf4j org.slf4j/slf4j-nop com.google.guava/guava]]
                 [com.fasterxml.jackson.core/jackson-core "2.11.2"]
                 [com.fasterxml.jackson.core/jackson-databind "2.11.2"]
                 [com.google.guava/guava "25.1-jre"]
                 [cprop "0.1.17"]
                 [expound "0.8.5"]
                 [funcool/struct "1.4.0"]
                 [io.rkn/conformity "0.5.1"]
                 [luminus-transit "0.1.2"]
                 [luminus-undertow "0.1.7"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [markdown-clj "1.10.5"]
                 [metosin/jsonista "0.2.6"]
                 [metosin/muuntaja "0.6.7"]
                 [metosin/reitit "0.5.5"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [nrepl "0.8.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.194"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.webjars.npm/bulma "0.9.0"]
                 [org.webjars.npm/material-icons "0.3.1"]
                 [org.webjars/webjars-locator "0.40"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.8.1"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.28"]]

  :min-lein-version "2.0.0"
  
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot booking.core

  :plugins [] 

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :uberjar-name "booking.jar"
             :source-paths ["env/prod/clj" ]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts ["-Dconf=dev-config.edn" ]
                  :dependencies [[pjstadig/humane-test-output "0.10.0"]
                                 [prone "2020-01-17"]
                                 [ring/ring-devel "1.8.1"]
                                 [ring/ring-mock "0.4.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]
                                 [jonase/eastwood "0.3.5"]] 
                  
                  :source-paths ["env/dev/clj" ]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user
                                 :timeout 120000}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn" ]
                  :resource-paths ["env/test/resources"] }
   :profiles/dev {}
   :profiles/test {}})
