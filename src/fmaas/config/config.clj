(ns fmaas.config.config
  (:require [nomad :refer [defconfig]]
            [clojure.java.io :as io]))

(defconfig config (io/resource "default-config.edn"))

(def get-config (config))

; FMAAS_HOME == "Where are my songs?"
; Com Port == "Where is my Arduino? COM3"
; Baud == "What baud is my Arduino at? 9600"
; Port == "What port should I start my webserver on? 8080"
; fix in-dev? in core.clj

