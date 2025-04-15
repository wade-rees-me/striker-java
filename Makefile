#!/usr/bin/zsh

# Define Gradle executable
GRADLE = ./gradlew
NATIVE = nativeCompile

# Output binary
TARGET = bin/strikerJava

# Home directory for Striker
STRIKER = ${HOME}/Striker

# Output JAR
TARGET_JAR = $(BIN_DIR)/striker.jar

# Define default target
.DEFAULT_GOAL := help

# Help target to show information about the Makefile
help:
	@echo "Makefile for Java Striker project"
	@echo ""
	@echo "Available targets:"
	@echo "  all           - Compile and build the JAR"
	@echo "  clean         - Remove class files and JAR"
	@echo "  lint          - Run static analysis on the source files"
	@echo "  install       - Install the compiled JAR"
	@echo "  run           - Run a simulation with specified parameters"
	@echo "  run-all       - Run all strategy/deck combinations"
	@echo ""
	@echo "Variables (can override via command line):"
	@echo "  HANDS=<n>     - Number of hands to simulate (default: 500000000)"
	@echo "  THREADS=<n>   - Number of threads (default: 24)"
	@echo "  STRATEGY=<s>  - Strategy to use (e.g. mimic, linear)"
	@echo "  DECKS=<s>     - Deck type (e.g. single-deck, six-shoe)"

# Build the project
build:
	$(GRADLE) build

# Run the project
run:
	$(GRADLE) run

# Run tests
test:
	$(GRADLE) test

# Clean the build
clean:
	$(GRADLE) clean

# Run lint checks
lint:
	$(GRADLE) check

# Compile for native execution
compile:
	$(GRADLE) $(NATIVE)
	cp ./build/native/nativeCompile/striker-java $(TARGET)

# Install the compiled binary to the Striker directory
install:
	cp $(TARGET) $(STRIKER)/bin

# Format all Java files using
format:
	@echo "Formatting Java files..."
	@find . -name "*.java" -exec echo {} \; -exec jfs {} \;
	@echo "Java files formatted."

include Makefile.run

