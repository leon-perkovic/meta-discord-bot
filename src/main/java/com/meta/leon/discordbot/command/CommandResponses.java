package com.meta.leon.discordbot.command;

/**
 * Class containing various response messages
 *
 * @author Leon, created on 18/03/2018
 */
public class CommandResponses{

    public static final String INVALID_COMMAND = "I don't know that command :cry:";

    public static final String NOT_AUTHORIZED = "Sorry, you don't have the rights to use that command :no_entry_sign:";

    public static final String PLAYER_NOT_FOUND = "That player doesn't exist :no_entry_sign:";

    public static final String PLAYER_ALREADY_EXISTS = "That player already exists :no_entry_sign:";

    public static final String ROLE_NOT_FOUND = "That role doesn't exist :no_entry_sign:";

    public static final String ROLE_ALREADY_EXISTS = "That role already exists :no_entry_sign:";

    public static final String EVENT_NOT_FOUND = "That event doesn't exist :no_entry_sign:";

    public static final String EVENT_ALREADY_EXISTS = "That event already exists :no_entry_sign:";

    // -- AddPlayer -----------------------------------------------------------

    public static final String ADD_PLAYER_INVALID_ARGUMENTS = "Expected at least 2 arguments :no_entry_sign: Try: ```!addPlayer <nickname> <account_name> [@username]```";

    public static final String ADD_PLAYER_INVALID_DISCORD_ID = "Invalid last argument :no_entry_sign: Try: ```!addPlayer <nickname> <account_name> [@username]```";

    // -- RemovePlayer --------------------------------------------------------

    public static final String REMOVE_PLAYER_INVALID_ARGUMENTS = "Expected 1 argument :no_entry_sign: Try: ```!removePlayer <id or nickname or @username>```";

    public static final String REMOVE_PLAYER_SUCCESS = "Player removed successfully :white_check_mark:";

    // -- GetPlayer -----------------------------------------------------------

    public static final String GET_PLAYER_INVALID_ARGUMENTS = "Expected 1 argument :no_entry_sign: Try: ```!getPlayer <id or nickname or @username>```";

    // -- GetPlayers ----------------------------------------------------------

    public static final String GET_PLAYERS_INVALID_ARGUMENTS = "Unexpected arguments :no_entry_sign: Try: ```!getPlayers```";

    public static final String GET_PLAYERS_NONE_FOUND = "I couldn't find any players :cry:";

    // -- UpdatePlayer --------------------------------------------------------

    public static final String UPDATE_PLAYER_INVALID_ARGUMENTS = "Expected at least 3 arguments :no_entry_sign: Try: ```!updatePlayer <id> <nickname> <account_name> [@username]```";

    public static final String UPDATE_PLAYER_INVALID_DISCORD_ID = "Invalid last argument :no_entry_sign: Try: ```!updatePlayer <id> <nickname> <account_name> [@username]```";


    public static final String UPDATE_PLAYER_INVALID_ID = "Expected first argument to be an ID (numeric) :no_entry_sign:";

    public static final String UPDATE_PLAYER_ALREADY_TAKEN = "Another player already has those properties :no_entry_sign:";

    // -- AddRole -------------------------------------------------------------

    public static final String ADD_ROLE_INVALID_ARGUMENTS = "Expected 2 arguments :no_entry_sign: Try: ```!addRole <role_name> <short_name>```";

    // -- RemoveRole ----------------------------------------------------------

    public static final String REMOVE_ROLE_INVALID_ARGUMENTS = "Expected 1 argument :no_entry_sign: Try: ```!removeRole <id or role_name or short_name>```";

    public static final String REMOVE_ROLE_SUCCESS = "Role removed successfully :white_check_mark:";

    // -- GetRole -------------------------------------------------------------

    public static final String GET_ROLE_INVALID_ARGUMENTS = "Expected 1 argument :no_entry_sign: Try: ```!getRole <id or role_name or short_name>```";

    // -- GetRoles ------------------------------------------------------------

    public static final String GET_ROLES_INVALID_ARGUMENTS = "Unexpected arguments :no_entry_sign: Try: ```!getRoles```";

    public static final String GET_ROLES_NONE_FOUND = "I couldn't find any roles :cry:";

    // -- UpdateRole ----------------------------------------------------------

    public static final String UPDATE_ROLE_INVALID_ARGUMENTS = "Expected 3 arguments :no_entry_sign: Try: ```!updateRole <id> <role_name> <short_name>```";

    public static final String UPDATE_ROLE_INVALID_ID = "Expected first argument to be an ID (numeric) :no_entry_sign:";

    public static final String UPDATE_ROLE_ALREADY_TAKEN = "Another role already has those properties :no_entry_sign:";

    // -- AddPr ---------------------------------------------------------------

    public static final String ADD_PR_INVALID_ARGUMENTS = "Expected at least 2 arguments :no_entry_sign: Try: ```!addPR <id or nickname or @username> <role_name or short_name ...>```";

    // -- AddEvent ------------------------------------------------------------

    public static final String ADD_EVENT_INVALID_ARGUMENTS = "Expected at least 3 arguments :no_entry_sign: Try: ```!addEvent <day> <HH:mm> <player_limit> [event_leader] [description]```";

    // -- RemoveEvent ---------------------------------------------------------

    public static final String REMOVE_EVENT_INVALID_ARGUMENTS = "Expected at least 1 argument :no_entry_sign: Try: ```!removeEvent <id or name or day> [HH:mm]```";

    public static final String REMOVE_EVENT_SUCCESS = "Event removed successfully :white_check_mark:";

    // -- GetEvent ------------------------------------------------------------

    public static final String GET_EVENT_INVALID_ARGUMENTS = "Expected at least 1 argument :no_entry_sign: Try: ```!getEvent <id or name or day> [HH:mm]```";

    // -- GetEvents -----------------------------------------------------------

    public static final String GET_EVENTS_INVALID_ARGUMENTS = "Unexpected arguments :no_entry_sign: Try: ```!getEvents```";

    public static final String GET_EVENTS_NONE_FOUND = "I couldn't find any events :cry:";

    // -- GetPastEvents -------------------------------------------------------

    public static final String GET_PAST_EVENTS_INVALID_ARGUMENT = "Invalid argument :no_entry_sign: Try: ```!getPastEvents [page_number]```";

}
