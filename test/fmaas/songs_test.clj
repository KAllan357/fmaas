(ns fmaas.songs-test
  (:require [clojure.test :refer :all]
            [fmaas.songs :refer :all]))

; TODO: Figure out how to test the other part of this or clause?
(deftest test-songs-dir
  (testing "FMAAS_HOME unset defaults to $HOME/songs"
    (is (= songs-dir (str (System/getenv "HOME") "/songs")))))

(deftest test-get-song-name
  (testing "Calls getName on a File and removes '.mid'"
    (is (= (get-song-name (clojure.java.io/file "/test.mid")) "test"))))

; TODO: Setup / Teardown of a tmpdir? Mocking?
