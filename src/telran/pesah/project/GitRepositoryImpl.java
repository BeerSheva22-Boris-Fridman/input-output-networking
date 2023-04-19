package telran.pesah.project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GitRepositoryImpl implements GitRepository {

	private static final long serialVersionUID = 1L;
	private static final String IGNORE_REGEX = "(^\\..*)|(^.*\\.jar)";
	private String head;
	private String currentCommitName;
	private String currentBranchName;

	private Map<String, FileToCommit> allCommittedFiles; // key - file name, value FileToCommit
	private Map<String, Commit> commits; // key - commit id, value - Commit
	private Map<String, String> branches; // key- name of a branch, value - commitID on this branch
	private Set<String> ignoredFileRegex;

	public GitRepositoryImpl() {
		allCommittedFiles = new HashMap<>();
		commits = new HashMap<>();
		branches = new HashMap<>();
		ignoredFileRegex = new HashSet<>();
		ignoredFileRegex.add(IGNORE_REGEX);
	}

	public static GitRepositoryImpl init(String dirPath) {
		if (new File(GIT_FILE).exists()) {
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(GIT_FILE)))) {
				return (GitRepositoryImpl) in.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return new GitRepositoryImpl();
	}

	@Override
	public String commit(String commitMessage) {
		// check two conditions. 1. list of all files in directory is empty 2. status of
		// all files in this list is Commited
		// if both or one of this conditions is tru then return messege "no files to
		// commit"
		if (info().isEmpty() || info().stream().allMatch(fs -> fs.status.equals(Status.COMMITED))) {
			return "no files to commit";
		}
		// check if head on commit and head not null
		if (getHead() == null && head != null) {
			return "Unable to commit with no branch.";
		}

		String commitId = generateId();
		Instant commitTime = Instant.now();
		CommitMessage comMessage = new CommitMessage(commitId, commitMessage);
		List<FileState> allFiles = info(); // get all files from current directory using method info
		List<FileToCommit> commitedFiles = new ArrayList<>(); // here we store files that have been ever commited
		for (FileState file : allFiles) {
			Path filePath = Paths.get(file.getFilePath());
			Instant fileModificationTime;
			try {
				fileModificationTime = Files.getLastModifiedTime(filePath).toInstant();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			String fileName = new File(file.filePath).getName();
			FileToCommit fileToCommit = new FileToCommit(fileName, file.getFilePath(),
					storeFileContent(file.getFilePath()), commitTime, fileModificationTime);
			commitedFiles.add(fileToCommit);
			allCommittedFiles.put(fileToCommit.getFileName(), fileToCommit);
		}
		String ParentId = currentCommitName;
		Commit newCommit = new Commit(generateId(), comMessage, commitTime, ParentId, commitedFiles);
		commits.put(commitId, newCommit);
		currentCommitName = commitId;

		if (head == null) {
			createBranch("master");
		}
		branches.replace(currentBranchName, currentCommitName);
		return "Commited " + commitedFiles.size() + " files" + "\n" + "Time of commit - " + commitTime + "\n"
				+ "Commit ID: " + currentCommitName + "\n" + "Commit is on the Branch: " + currentBranchName;

	}

	private String generateId() {
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 7; i++) {
			char randomChar = (char) (random.nextInt(26) + 'a');
			builder.append(randomChar);
		}
		return builder.toString();
	}

	public static List<String> storeFileContent(String filePath) {
		Path path = Paths.get(filePath);
		List<String> fileWithContent = null;
		try {
			fileWithContent = Files.readAllLines(path, StandardCharsets.ISO_8859_1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileWithContent;
	}

	@Override
	public String createBranch(String branchName) {
	//	if (branches.containsKey(branchName) || commits.containsKey(branchName)) {
		if (branches.containsKey(branchName)) {	
			return "This name is alreade in use";
		}
		branches.put(branchName, currentCommitName);
		head = branchName;
		currentBranchName = branchName;
		return "Branch " + "\"" + branchName + "\"" + " created. Now it is a active branch";

	}

	@Override
	public String renameBranch(String branchName, String newName) {
		if (!branches.containsKey(branchName)) {
			return "Branch with name" + "\""+ branchName + "\"" + " does not exists";
		}
		if (branches.containsKey(newName)) {
			return "This name is already busy";
		}

		if (branchName.equals(currentBranchName)) {
			return "can't rename current branch";
		}		
		String commitName = branches.get(branchName);
		branches.put(newName, commitName);
		branches.remove(branchName);		
		
	
		return "Branch " + "\""+ branchName + "\""+ " renamed to " + "\""+ newName + "\"";
	}

	@Override
	public String deleteBranch(String branchName) {
		if (!branches.containsKey(branchName)) {
			return "Branch with such a name does not exist";
		}
		if (branchName.equals(currentBranchName)) {
			return "Could not delite a current branch";
		}
		branches.remove(branchName);
		return "Branch " + "\"" + branchName + "\"" + "Deleted";
	}

	@Override
	public List<String> branches() {
		Set<String> BranchNames = new HashSet<>(branches.keySet());
		if (!BranchNames.remove(currentBranchName)) {
			return new ArrayList<>();
		} else {
			String nameWithAsterix = currentBranchName + " *";
			BranchNames.add(nameWithAsterix);
			List<String> res = new ArrayList<>(BranchNames);
			return res;
			
		}
	}

	@Override
	public List<Path> commitContent(String commitId) {
		List<Path> paths = new ArrayList<>();
		if (commits.containsKey(commitId)) {
			commits.get(commitId).getCommitedFiles().forEach(file -> paths.add(Path.of(file.getFilePath())));
		}
		return paths;
	}

	@Override
	public String switchTo(String name) {
		// проверяем содержится ли нейм в ветках
		// если имя равно хеду, то выводим сообщение, что switching to the current commit
		// doesn’t make a sense
		// проверяем все ли файлы закомичены, если нет, то возвращаем сообщение switchTo
		// may be done only after commit
		// берем айди комиита из мапы веток по ключу нейм
		// далее по айдишнику коммита берем из мапы коммитов нужный коммит и у него
		// берем лист файлов
		// вызываем метод инфо
		// удаляем все файлы из директории
		// записываем в директорию все файлы из коммита (см стр.218)

		// если нейм не имя ветки, а имя коммита то делаем тоже самое начиная с стр.
		// 218:
		// записываем в хед нейм в любом случае
		if (name == head || name == branches.get(name)) {
			return "Switching to the current commit doesn’t make a sense"; // !!!!!!!CHECK!!!!!!
		}
		if (!info().stream().allMatch(fs -> fs.status.equals(Status.COMMITED))) {
			return "SwitchTo may be done only after commit";
		}
		if (!branches.containsKey(name) && !commits.containsKey(name)) {
			return "No such a name of branch or commit";
		}
		if (branches.containsKey(name)) {
			String commitIdToRestore = branches.get(name);
			List<FileToCommit> filesToRestore = commits.get(commitIdToRestore).getCommitedFiles();
			cleanDerectory();
			restore(filesToRestore);
			head = name;
			currentCommitName = commitIdToRestore;
			return "Branch chenged to " + head;
		}

		if (commits.containsKey(name)) {
			List<FileToCommit> filesToRestore = commits.get(name).getCommitedFiles();
			cleanDerectory();
			restore(filesToRestore);
			head = name;
			currentCommitName = name;
		}
		return "Commit chenged to " + head;
	}

	private void cleanDerectory() {
		List<FileState> filesToDelite = info();
		for (FileState fileToDelite : filesToDelite) {
			String pathToDelite = fileToDelite.getFilePath();
			File delite = new File(pathToDelite);
			delite.delete();
		}

	}

	private void restore(List<FileToCommit> filesToRestore) {
		for (FileToCommit fileToRestore : filesToRestore) {
			List<String> contentToRestore = fileToRestore.getStoreFileContent();
			Instant fileTime = fileToRestore.getFileModificationTime();
			Path filePath = Paths.get(fileToRestore.getFilePath());
			try {
				Files.createFile(filePath);
				Files.write(filePath, contentToRestore);
				Files.setLastModifiedTime(filePath, FileTime.from(fileTime));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public String getHead() {
		return branches.containsKey(head) ? head : null;
	}

	@Override
	public void save() {
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File(GIT_FILE)));
			output.writeObject(this);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
	}

	@Override
	public String addIgnoredFileNameExp(String regex) {
		try {
			"vasya".matches(regex);
			ignoredFileRegex.add(regex);
			return "This file will be ignored";
		} catch (PatternSyntaxException e) {
			return "Such an expression could not be added";
		}
	}

	public boolean isFileIgnored(String file) {
		return ignoredFileRegex.stream().anyMatch(file::matches);
	}

	@Override
	public List<FileState> info() {
		ArrayList<FileState> resFileStates = new ArrayList<FileState>();
		try {
			String currentDir = "C:\\Users\\asus\\eclipse-workspace\\input-output-networking\\myDirectory1\\Git";
			List<String> allFiles = allFilesInCurrentDir(currentDir);
			for (String filePath : allFiles) {
				String fileName = new File(filePath).getName();
//				if (!ignoredFileRegex.contains(fileName)) {
				if (!ignoredFileRegex.stream().anyMatch(fileName::matches)) {
					if (!allCommittedFiles.containsKey(fileName)) {
						FileState fileState = new FileState(filePath, Status.UNTRACKED, fileName);
						resFileStates.add(fileState);
					} else {
						long lastModifiedAllFiles = new File(filePath).lastModified();
						long timeOfFileCommit = allCommittedFiles.get(fileName).getTimeOfCommit().toEpochMilli();
						if (lastModifiedAllFiles < timeOfFileCommit) {
							FileState fileState = new FileState(filePath, Status.COMMITED, fileName);
							resFileStates.add(fileState);
						} else {
							FileState fileState = new FileState(filePath, Status.MODIFIED, fileName);
							resFileStates.add(fileState);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resFileStates;
	}

	public List<String> allFilesInCurrentDir(String currentDir) throws IOException {
		List<String> filesPathes = new ArrayList<>();
		Files.walk(Paths.get(currentDir)).forEach(file -> {
			if (!Files.isDirectory(file)) {
				filesPathes.add(file.toAbsolutePath().toString());
			}
		});
		return filesPathes;
	}

	@Override
	public List<CommitMessage> log() {
		if (commits.isEmpty())
			return new ArrayList<>();

		List<CommitMessage> list = new ArrayList<>();
		list.add(commits.get(currentCommitName).getCommitMessage());
		String prevID = commits.get(currentCommitName).getParentId();
		while (prevID != null) {
			list.add(commits.get(prevID).getCommitMessage());
			prevID = commits.get(prevID).getParentId();
		}
		return list;
	}

}
