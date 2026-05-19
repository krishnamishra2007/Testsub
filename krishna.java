class BioAnalyzer:
            return "RNA"
        elif s.issubset(protein):
            return "PROTEIN"
        else:
            return "UNKNOWN"

    # ✔️ BASE COUNT (DNA + RNA)
    def count_all(self):
        return {
            "A": self.seq.count("A"),
            "T": self.seq.count("T"),
            "G": self.seq.count("G"),
            "C": self.seq.count("C"),
            "U": self.seq.count("U")
        }

    # ✔️ PROTEIN COUNT
    def protein_count(self):
        aas = "ARNDCEQGHILKMFPSTWYV"
        return {aa: self.seq.count(aa) for aa in aas}
import os
from fastaparser import BioAnalyzer


INPUT_FOLDER = "input"
OUTPUT_FOLDER = "output"


def read_fasta(file_path):
    sequences = {}

    with open(file_path, "r") as f:
        seq_id = None
        seq_data = []

        for line in f:
            line = line.strip()

            if not line:
                continue

            if line.startswith(">"):
                if seq_id is not None:
                    sequences[seq_id] = "".join(seq_data)

                seq_id = line[1:]
                seq_data = []
            else:
                seq_data.append(line.upper())

        if seq_id is not None:
            sequences[seq_id] = "".join(seq_data)

    return sequences


def save_output(filename, content):

    if not os.path.exists(OUTPUT_FOLDER):
        os.makedirs(OUTPUT_FOLDER)

    path = os.path.join(OUTPUT_FOLDER, filename)

    with open(path, "w") as f:
        f.write(content)

    return path


def main():

    files = [
        os.path.join(INPUT_FOLDER, f)
        for f in os.listdir(INPUT_FOLDER)
        if f.endswith(".fasta")
    ]

    if not files:
        print("No FASTA files found in input folder")
        return

    print("\nBIO ANALYSIS ENGINE STARTED")
    print("=" * 60)

    for file in files:

        print("\nProcessing:", file)

        sequences = read_fasta(file)

        report = []

        report.append(f"FILE: {file}")
        report.append("-" * 60)

        for seq_id, seq in sequences.items():

            bio = BioAnalyzer(seq)

            seq_type = bio.detect_type()

            print(seq_id, seq_type, len(seq))

            report.append(f"\nID: {seq_id}")
            report.append(f"TYPE: {seq_type}")
            report.append(f"LENGTH: {len(seq)}")

            if seq_type in ["DNA", "RNA"]:
                report.append(f"BASE COUNT: {bio.count_all()}")
            else:
                report.append(f"AA COUNT: {bio.protein_count()}")

            report.append("-" * 40)

        out_name = os.path.basename(file).replace(".fasta", ".txt")

        path = save_output(out_name, "\n".join(report))

        print("Saved:", path)


if __name__ == "__main__":
    main() 