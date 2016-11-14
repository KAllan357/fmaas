(ns fmaas.receivers.floppy-drive-test
  (:import (javax.sound.midi ShortMessage))
  (:require [clojure.test :refer :all]
            [fmaas.receivers.floppy-drive :refer :all]))

(deftest test-note-off?
  (testing "Detects a note off given a number > 127 and < 144"
    (is (false? (note-off? 127)))
    (is (note-off? 128))
    (is (note-off? 138))
    (is (false? (note-off? 144)))
    (is (false? (note-off? 1999)))))

(deftest test-note-on?
  (testing "Detects a note on given a number > 143 and < 160"
    (is (false? (note-on? 143)))
    (is (note-on? 144))
    (is (note-on? 155))
    (is (false? (note-on? 160)))
    (is (false? (note-on? 1999)))))

(deftest test-pitch-bend?
  (testing "Detects a pitch bend given a number > 223 and < 240"
    (is (false? (pitch-bend? 223)))
    (is (pitch-bend? 224))
    (is (pitch-bend? 235))
    (is (false? (pitch-bend? 240)))
    (is (false? (pitch-bend? 1999)))))

(deftest test-get-pitch-bend
  (testing "Calculates a pitch-bend message into a value useful for floppys"
    (let [message (ShortMessage.)]
      (is (= 16320 (get-pitch-bend message))))))

(deftest test-pitch-bend-period
  (testing "Soemthing"
    (let [message (ShortMessage. 227 10 10)
          p 16320]
      (is (set-current-period 3 3)
          (= 2 (pitch-bend-period message p))))))
