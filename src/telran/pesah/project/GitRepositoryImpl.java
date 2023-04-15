package telran.pesah.project;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class GitRepositoryImpl implements GitRepository {

	private static final long serialVersionUID = 1L;

	private static final String IGNORE_FILE = ".gitignore";
	private String head;
	private String currentId;
	private Map<String,FileToCommit> allCommittedFiles; //key - file name, value FileToCommit
	private Map<String, Commit> commits; // key - commit id, value - Commit
	private Map<String, String> branches; // key- name of a branch, value - commitID on this branch
	private Set<String> ignoredFileRegex;

	private String currentBranchName;

	private String currentCommitName;

	public GitRepositoryImpl() {
		head = null;
		allCommittedFiles = new HashMap<>();
		commits = new HashMap<>();
		branches = new HashMap<>();
		ignoredFileRegex = new HashSet<>();
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
		//check if head on commit and head not null
		if(getHead() == null && head != null) {
			return "Unable to commit with no branch.";
		}
		
		String commitId = generateId();
		Instant commitTime = Instant.now();
		CommitMessage comMessage = new CommitMessage(commitId, commitMessage);
		List<FileState> allFiles = info(); //get all files from current directory using method info
		List<FileToCommit> commitedFiles = new ArrayList<>(); //here we store files that have been ever commited
		for (FileState file : allFiles) {
//			if (!(file.getStatus().equals(Status.COMMITED))) {
				String fileName = new File(file.filePath).getName();
				FileToCommit fileToCommit = new FileToCommit(fileName, file.getFilePath(),
						storeFileContent(file.getFilePath()), commitTime);
				commitedFiles.add(fileToCommit);
				allCommittedFiles.put(fileToCommit.getFileName(), fileToCommit);

//			}
		}
		String ParentId = currentId;
		Commit newCommit = new Commit(generateId(), comMessage, commitTime, ParentId, commitedFiles);
		commits.put(commitId, newCommit);
		currentCommitName = commitId;
		
		if (head == null) {
			createBranch("master");
		}
		branches.replace(currentBranchName, currentCommitName);
		return "Commited " + commitedFiles.size() + " files" + 
		"\n" + "Time of commit - " + commitTime +
		"\n" + "Commit ID: " +currentCommitName +
		"\n" + "Commit is on the Branch: " + currentBranchName;

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

//	private boolean getWorkingDirectoryFileStatus() {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public String createBranch(String branchName) {
		if (branches.containsKey(branchName) || commits.containsKey(branchName)) {
			return "This name is alreade in use";
		}
		
		//Branch branch = new Branch(branchName, currentCommitName);
		branches.put(branchName, currentCommitName);
		
		head = branchName;
		currentBranchName = branchName;
		
//		switchTo(branchName);
		
		return "Branch " + "\"" + branchName + "\"" + " created. Now it is a active branch";

	}

	@Override
	public String renameBranch(String branchName, String newName) {
//		// Check if the branch exists
//	    if (!branches().contains(branchName)) {
//	        return "Branch " + branchName + " does not exist";
//	    }
//
//	    // Check if the new name is valid and doesn't already exist
//	    if (newName == null || newName.isEmpty()) {
//	        return "Invalid branch name";
//	    }
//	    if (branches().contains(newName)) {
//	        return "Branch " + newName + " already exists";
//	    }
//
//	    // Rename the branch
//	    for (Commit commit : commits) {
//	        if (commit.getBranch().equals(branchName)) {
//	            commit.setBranch(newName);
//	        }
//	    }
//	    if (currentBranch.equals(branchName)) {
//	        currentBranch = newName;
//	    }

		return "Branch " + branchName + " renamed to " + newName;
	}

	@Override
	public String deleteBranch(String branchName) {
//		 if (!branches.containsKey(branchName)) {
//		        return "Branch " + branchName + " does not exist";
//		    }
//		    if (branchName.equals(currentBranch)) {
//		        return "Cannot delete current branch";
//		    }
//
//		    branches.remove(branchName);
		return "Deleted branch " + branchName;
	}

	@Override
	public List<String> branches() {
		List<String> branchNames = new ArrayList<>(branches.keySet());
		String currentBranchName = getCurrentBranchName();

		// Add an asterisk (*) to the current branch name
		for (int i = 0; i < branchNames.size(); i++) {
			if (branchNames.get(i).equals(currentBranchName)) {
				branchNames.set(i, currentBranchName + "*");
			}
		}

		return branchNames;
	}

	private String getCurrentBranchName() {
//	    for (Map.Entry<String, String> entry : branches.entrySet()) {
//	        if (entry.getValue().equals(headCommit)) {
//	            return entry.getKey();
//	        }
//	    }
		return null;
	}

	@Override
	public List<Path> commitContent(String commitId) {
	List<Path> paths = new ArrayList<>();
		if(commits.containsKey(commitId)) {
			commits.get(commitId).getCommitedFiles().forEach(file -> paths.add(Path.of(file.getFilePath())));
		}
		return paths;
	}

	@Override
	public String switchTo(String name) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FileState> info() {
		ArrayList<FileState> resFileStates = new ArrayList<FileState>();
		try {
			String currentDir = "C:\\Users\\asus\\eclipse-workspace\\input-output-networking\\myDirectory1\\Git";
			List<String> allFiles = allFilesInCurrentDir(currentDir);
			for (String filePath : allFiles) {
				String fileName = new File(filePath).getName();
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
		if (commits.isEmpty()) return new ArrayList<>();
		
		List<CommitMessage> list = new ArrayList<>();
		list.add(commits.get(currentId).getCommitMessage());
		String prevID = commits.get(currentId).getParentId();
		while(prevID != null) {
			list.add(commits.get(prevID).getCommitMessage());
			prevID = commits.get(prevID).getParentId();
		}
		return list;
	}

}
