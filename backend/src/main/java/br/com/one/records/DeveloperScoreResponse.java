package br.com.one.records;

public record DeveloperScoreResponse (
	Long developerId,
	String developerName,
	Long totalPoints
) {}
