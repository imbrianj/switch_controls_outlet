/**
 *  Switch Controls Outlet
 *
 *  Author: brian@bevey.org
 *  Date: 2013-11-16
 *
 *  A Z-Wave switch controls any given outlets.  Allows independent control of
 *  outlets by turning the switch off when already in an off state.
 */
preferences {
  section("Turn on with which switch?") {
    input "wallSwitch", "capability.switch"
  }

  section("Turns on which outlets?") {
    input "outlets", "capability.switch", multiple: true
  }

  section("If switch is off, use off as toggle for outlets?") {
    input "offToggle", "enum", metadata: [values: ["Yes", "No"]], required: false
  }
}

def installed() {
  init()
}

def updated() {
  unsubscribe()
  init()
}

def init() {
  subscribe(wallSwitch, "switch", changeLights)

  if(offToggle == "Yes") {
    subscribe(wallSwitch, "switch.off", doubleSwitch, [filterEvents: false])
  }
}

def changeLights(evt) {
  if(evt.value == "on") {
    log.info("Turning on lights")

    outlets?.on()
  }

  else {
    log.info("Turning off lights")

    outlets?.off()
  }
}

def doubleSwitch(evt) {
  log.info("Switch is off, but we want to toggle outlets")

  if(outlets.findAll { it?.latestValue("switch") == "on" }) {
    log.info("Toggle lights off")

    outlets?.off()
  }

  else {
    log.info("Toggle lights on")

    outlets?.on()
  }
}