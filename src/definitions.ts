export interface VeriffPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
