package jm.controller.rest;

import jm.BotService;
import jm.WorkspaceService;
import jm.model.Bot;
import jm.model.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/rest/api/bot")
public class BotRestController {

    private BotService botService;
    private WorkspaceService workspaceService;

    private static final Logger logger = LoggerFactory.getLogger(
            BotRestController.class);

    @Autowired
    public void setBotService(BotService botService) { this.botService = botService; }

    @Autowired
    public void setWorkspaceService(WorkspaceService workspaceService) { this.workspaceService = workspaceService; }

    @GetMapping("/workspace/{id}")
    public ResponseEntity<Bot> getBotByWorksapce(@PathVariable("id") Long id) {
        Workspace workspace = workspaceService.getWorkspaceById(id);
        Bot bot = botService.GetBotByWorkspaceId(workspace);
        if(bot == null) {
            logger.warn("Не удалось найти бота для workspace с id = {}", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Бот для workspace c id = {}", id);
        logger.info(bot.toString());
        return new ResponseEntity<Bot>(bot, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bot> getBotById(@PathVariable("id") Long id) {
        logger.info("Бот с id = {}", id);
        logger.info(botService.getBotById(id).toString());
        return new ResponseEntity<Bot>(botService.getBotById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity createBot(@RequestBody Bot bot) {
        try {
           botService.createBot(bot);
            logger.info("Cозданный bot: {}", bot);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            logger.warn("Не удалось создать бота");
            ResponseEntity.badRequest().build();
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity updateBot(@RequestBody Bot bot) {
        Bot existingBot = botService.getBotById(bot.getId());
        if (existingBot == null) {
            logger.warn("Бот не найден");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
           botService.updateBot(bot);
           logger.info("Обновлнный бот: {}", bot);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBot(@PathVariable("id") Long id) {
       botService.deleteBot(id);
       logger.info("Удален бот с id = {}", id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
